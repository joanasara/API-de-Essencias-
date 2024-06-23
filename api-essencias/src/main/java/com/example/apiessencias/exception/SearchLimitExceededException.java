package com.example.apiessencias.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class SearchLimitExceededException extends RuntimeException {

    public SearchLimitExceededException(String message) {
        super(message);
    }

}
