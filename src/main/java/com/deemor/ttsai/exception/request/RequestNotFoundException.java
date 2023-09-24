package com.deemor.ttsai.exception.request;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestNotFoundException extends BusinessException {
    public RequestNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Request not found");
    }
}
