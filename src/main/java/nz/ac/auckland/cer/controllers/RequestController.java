package nz.ac.auckland.cer.controllers;


import nz.ac.auckland.cer.model.requests.VMConsultation;
import okhttp3.*;
import okhttp3.ResponseBody;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
public class RequestController {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient.Builder builder;
    private OkHttpClient client;

    @Value("${service-now.base-url}")
    private String baseUrl;

    @Value("${service-now.user}")
    private String user;

    @Value("${service-now.password}")
    private String password;

    @Value("${service-now.cer.vm.assignment-group-id}")
    private String cerVmAssignmentGroupId;

    @Value("${service-now.cer.vm.business-service-id}")
    private String cerVmBusinessServiceId;

    @Value("${service-now.cer.vm.watch-list}")
    private String[] cerVmWatchList;

    RequestController() {
        builder = new OkHttpClient.Builder();
        builder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(user, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        client = builder.build();
    }

    private ResponseBody post(String url, String json) throws IOException {
        okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body();
    }

    /*
     * Create ServiceNow VM Consultation ticket
     */

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/vmConsultation/create")
    String createVMConsultationRequest(@RequestBody VMConsultation vmConsultation) {
        String requestorUpi = "jdip004"; // TODO replace with upi from Shiboleth headers
        String url = baseUrl + "/api/now/table/u_request";
        JSONObject response = new JSONObject();

        try {
            // Create template
            String templatePath = getClass().getClassLoader().getResource("servicenow_consultation_vm.tpl").getFile();
            String template = new String(Files.readAllBytes(Paths.get(templatePath)));
            StringTemplate ticketComments = new StringTemplate(template, DefaultTemplateLexer.class);
            ticketComments.setAttribute("requestorUpi", requestorUpi);
            ticketComments.setAttribute("time", vmConsultation.getDate());
            ticketComments.setAttribute("date", vmConsultation.getTime());
            ticketComments.setAttribute("comments", vmConsultation.getComments());

            // Create ticket body
            JSONObject body = new JSONObject()
                    .put("u_requestor", requestorUpi)
                    .put("assignment_group", cerVmAssignmentGroupId)
                    .put("category", "Research IT")
                    .put("subcategory", "Research Computing Platforms")
                    .put("u_business_service", cerVmBusinessServiceId)
                    .put("short_description", "Research VM consultation request: " + requestorUpi)
                    .put("comments", ticketComments.toString())
                    .put("watch_list", String.join(",", cerVmWatchList));

            try {
                // Submit ticket
                ResponseBody responseBody = post(url, body.toString());
                JSONObject serviceNowResponse = new JSONObject(responseBody.string()).getJSONObject("result");
                response.put("ticketNumber", serviceNowResponse.getString("number"));
                response.put("ticketUrl", baseUrl + "/nav_to.do?uri=/u_request.do?sys_id=" + serviceNowResponse.getString("sys_id"));
            } catch (IOException e) {
                response.put("error", "There was an error communicating with ServiceNow");
                System.out.println(e.getMessage());
            } catch (JSONException e) {
                response.put("error", "There was an error processing the ServiceNow response");
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            response.put("error", "There was an error reading the ticket template");
            System.out.println(e.getMessage());
        }

        return response.toString();
    }
}
