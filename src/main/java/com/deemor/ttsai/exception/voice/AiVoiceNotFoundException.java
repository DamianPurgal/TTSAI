package com.deemor.ttsai.exception.voice;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AiVoiceNotFoundException extends BusinessException {
    public AiVoiceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Voice not found");
    }
}
