package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.ConversationModelApi;
import com.deemor.ttsai.dto.conversation.ConversationDto;
import com.deemor.ttsai.entity.conversation.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ConversationMapper {

    ConversationModelApi mapDtoToModelApi(ConversationDto conversationDto);
    List<ConversationModelApi> mapDtoToModelApi(List<ConversationDto> conversationDto);

    ConversationDto mapModelApiToDto(ConversationModelApi conversationModelApi);
    List<ConversationDto> mapModelApiToDto(List<ConversationModelApi> conversationDto);

    Conversation mapDtoToEntity(ConversationDto conversationDto);
    List<Conversation> mapDtoToEntity(List<ConversationDto> conversationDto);

    ConversationDto mapEntityToDto(Conversation conversation);
    List<ConversationDto> mapEntityToDto(List<Conversation> conversationDto);

}
