package com.deemor.ttsai.exception.request;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestNotValidException extends BusinessException {
    public RequestNotValidException() {
        super(HttpStatus.NOT_FOUND, "Request not valid");
    }

    public RequestNotValidException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
