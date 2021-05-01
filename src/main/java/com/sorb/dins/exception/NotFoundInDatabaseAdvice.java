package com.sorb.dins.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundInDatabaseAdvice {
    @ResponseBody
    @ExceptionHandler(NotFoundInDatabaseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(NotFoundInDatabaseException ex) {
        return ex.getMessage();
    }
}
