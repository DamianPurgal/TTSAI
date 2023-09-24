package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.RequestModelApi;
import com.deemor.TTSAI.model.RequestPageModelApi;
import com.deemor.ttsai.dto.request.RequestDto;
import com.deemor.ttsai.dto.request.RequestPage;
import com.deemor.ttsai.entity.request.Request;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RequestMapper {

    Request mapToEntity(RequestDto request);

    RequestDto mapDtoToModelApi(RequestModelApi requestModelApi);

    RequestDto mapToDto(Request request);
    List<RequestDto> mapToDto(List<Request> request);

    RequestModelApi mapDtoToModelApi(RequestDto request);
    List<RequestModelApi> mapDtoToModelApi(List<RequestDto> request);

    RequestPageModelApi mapRequestPageToModelApi(RequestPage requestPage);
}
