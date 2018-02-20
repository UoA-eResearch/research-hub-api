package nz.ac.auckland.cer.controllers;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private ErrorAttributes errorAttributes;

    @Autowired
    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        JSONObject responseBody = new JSONObject();
        responseBody.put("status", status.value());
        responseBody.put("statusText", status.getReasonPhrase());

        logger.error("status: " + status.value() + ", statusText: " + status.getReasonPhrase());
        logger.error(ex.toString());

        return new ResponseEntity<>(responseBody.toString(), headers, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleBaseException(Exception e, WebRequest webRequest, HttpServletRequest httpServletRequest) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(e, null, new HttpHeaders(), httpStatus, webRequest);
    }
}