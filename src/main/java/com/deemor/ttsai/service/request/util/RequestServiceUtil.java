package com.deemor.ttsai.service.request.util;

import com.deemor.ttsai.dto.conversation.ConversationDto;
import com.deemor.ttsai.exception.request.RequestNotValidException;

import java.util.List;

public class RequestServiceUtil {

    public static void validateConversation(List<ConversationDto> conversation) {
        if (conversation == null) {
            throw new RequestNotValidException("Conversation is null");
        }
        if (conversation.isEmpty()) {
            throw new RequestNotValidException("Conversation is empty");
        }
        conversation.forEach(message -> {
            if (message.getMessage() == null || message.getVoiceType() == null) {
                throw new RequestNotValidException("Conversation message or voice is null");
            } else if (message.getMessage().isBlank() || message.getVoiceType().isBlank()) {
                throw new RequestNotValidException("Conversation message or voice is empty");
            }
        });
    }
}
