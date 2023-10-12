package com.deemor.ttsai.exception.elevenlabs;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ElevenLabsConnectionException extends BusinessException {
    public ElevenLabsConnectionException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "ElevenLabs service unavailable");
    }
}
