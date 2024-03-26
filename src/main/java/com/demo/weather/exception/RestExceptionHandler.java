package com.demo.weather.exception;

import com.demo.weather.model.WarningType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomRequestException.class)
    protected ResponseEntity<Object> handleNotValidRequestException(CustomRequestException ex) {
        /* TODO This is an example based on two operations, POST and PUT. Replace this code based on your service functionality. */

        String validationErrorProblemType = ex.getMessage();
        WarningType warningType = ex.getWarningType();
        return new ResponseEntity<>(warningType, HttpStatus.BAD_REQUEST);
    }



}
