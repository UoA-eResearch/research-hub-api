package nz.ac.auckland.cer.controllers;


import nz.ac.auckland.cer.model.requests.VMConsultation;
import okhttp3.*;
import okhttp3.ResponseBody;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RestController
public class RequestController {

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

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
    String createVMConsultationRequest(@RequestAttribute(value="uid", required = false) String requestorUpi, @RequestBody VMConsultation vmConsultation, HttpServletResponse httpServletResponse) {
        String url = baseUrl + "/api/now/table/u_request";
        JSONObject response = new JSONObject();

        if (requestorUpi != null) {
            try {
                // Generate comments based on template
                ClassPathResource res = new ClassPathResource("servicenow_consultation_vm.tpl");
                String template = new String(FileCopyUtils.copyToByteArray(res.getInputStream()), StandardCharsets.UTF_8);
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
                    response.put("error", "IOException: there was an error communicating with ServiceNow");
                    logger.error(e.toString());
                } catch (JSONException e) {
                    response.put("error", "JSONException: there was an error parsing the ServiceNow response");
                    logger.error(e.toString());
                }
            } catch (IOException e) {
                response.put("error", "There was an error reading the ticket template");
                logger.error(e.toString());
            }
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String message = "User not logged in: 'uid' request attribute not found";
            logger.error(message);
            response.put("error", message);
        }

        return response.toString();
    }
}
