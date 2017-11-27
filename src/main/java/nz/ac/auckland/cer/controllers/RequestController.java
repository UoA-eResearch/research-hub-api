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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

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
                if (responseCount(response) >= 3) {
                    return null; // If we've failed 3 times, give up. - in real life, never give up!!
                }

                String credential = Credentials.basic(user, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        client = builder.build();
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
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
    ResponseEntity<Object> createVMConsultationRequest(@RequestAttribute(value = "uid", required = false) String requestorUpi, @RequestBody VMConsultation vmConsultation) throws IOException {
        String url = baseUrl + "/api/now/table/u_request";
        JSONObject response = new JSONObject();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

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
            JSONObject serviceNowResponse = new JSONObject(responseBody.string());

            if (!serviceNowResponse.isNull("result")) {
                JSONObject result = serviceNowResponse.getJSONObject("result");
                httpStatus = HttpStatus.OK;
                response.put("ticketNumber", result.getString("number"));
                response.put("ticketUrl", baseUrl + "/nav_to.do?uri=/u_request.do?sys_id=" + result.getString("sys_id"));
            } else if (!serviceNowResponse.isNull("error")) {
                JSONObject error = serviceNowResponse.getJSONObject("error");
                response.put("status", httpStatus.value());
                response.put("statusText", httpStatus.getReasonPhrase());
                response.put("error", "ServiceNow internal error");
                response.put("message", error.getString("message"));
                response.put("detail", error.getString("detail"));
                logger.error(response.toString());
            }
        } catch (IOException e) {
            response.put("status", httpStatus.value());
            response.put("statusText", httpStatus.getReasonPhrase());
            response.put("error", "Error communicating with ServiceNow");
            response.put("message", e.getMessage());
            response.put("detail", e.getStackTrace());
            logger.error(response.toString());
        } catch (JSONException e) {
            response.put("status", httpStatus.value());
            response.put("statusText", httpStatus.getReasonPhrase());
            response.put("error", "Error reading ServiceNow response");
            response.put("message", e.getMessage());
            response.put("detail", e.getStackTrace());
            logger.error(response.toString());
        }

        return new ResponseEntity<>(response.toString(), httpStatus);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<Object> handleMissingParams(ServletRequestBindingException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        JSONObject response = new JSONObject();
        response.put("status", httpStatus.value());
        response.put("statusText", httpStatus.getReasonPhrase());
        response.put("error", "User is not authenticated with UoA Single Sign On");
        response.put("message", e.getMessage());
        response.put("detail", e.getStackTrace());
        logger.error(response.toString());

        return new ResponseEntity<>(response.toString(), httpStatus);
    }
}
