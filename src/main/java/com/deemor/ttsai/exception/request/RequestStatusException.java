package com.deemor.ttsai.exception.request;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestStatusException extends BusinessException {
    public RequestStatusException() {
        super(HttpStatus.BAD_REQUEST, "Cannot change request status");
    }
}