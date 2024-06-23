package com.example.apiessencias.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EssenciaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SearchLimitExceededException.class)
    public ResponseEntity<String> handleRateLimitExceededException(SearchLimitExceededException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Ocorreu um erro. Tente novamente mais tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}