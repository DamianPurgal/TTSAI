package com.deemor.ttsai.converter;

import com.deemor.ttsai.entity.conversation.Conversation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Converter
public class ConversationListConverter implements AttributeConverter<List<Conversation>, String> {

    @Override
    public String convertToDatabaseColumn(List<Conversation> objects) {
        ObjectMapper objectMapper = new ObjectMapper();
        String conversationListJson = null;

        if (objects == null) {
            objects = new ArrayList<>();
        }

        try {
            conversationListJson = objectMapper.writeValueAsString(objects);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return conversationListJson;
    }

    @Override
    public List<Conversation> convertToEntityAttribute(String conversationListJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Conversation> conversationList = null;

        try {
            if (conversationListJson != null) {
                conversationList = objectMapper.readValue(conversationListJson, new TypeReference<ArrayList<Conversation>>() {});
            }

        } catch (final IOException e) {
            System.out.println("JSON reading error");
        }

        return conversationList;
    }

}