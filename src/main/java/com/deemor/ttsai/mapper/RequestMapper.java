package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.RequestModelApi;
import com.deemor.TTSAI.model.RequestPageModelApi;
import com.deemor.ttsai.dto.request.RequestDto;
import com.deemor.ttsai.dto.request.RequestPage;
import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.request.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class}
)
public interface RequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfCreation", expression = "java(LocalDateTime.now())")
    @Mapping(target = "voiceType", expression = "java(request.getVoiceType().toUpperCase())")
    @Mapping(target = "requestStatus", constant = "NEW")
    Request mapToEntity(RequestDto request);

    @Mapping(target = "voiceType", expression = "java(requestModelApi.getVoiceType().toUpperCase())")
    RequestDto mapDtoToModelApi(RequestModelApi requestModelApi);

    RequestDto mapToDto(Request request);

    List<RequestDto> mapToDto(List<Request> request);

    RequestModelApi mapDtoToModelApi(RequestDto request);

    List<RequestModelApi> mapDtoToModelApi(List<RequestDto> request);

    RequestPageModelApi mapRequestPageToModelApi(RequestPage requestPage);

    Alert mapToAlert(Request request);
}
