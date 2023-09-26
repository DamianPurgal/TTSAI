package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.AlertModelApi;
import com.deemor.TTSAI.model.AlertPageModelApi;
import com.deemor.TTSAI.model.RequestModelApi;
import com.deemor.TTSAI.model.RequestPageableModelApi;
import com.deemor.ttsai.dto.alert.AlertDto;
import com.deemor.ttsai.dto.alert.AlertPage;
import com.deemor.ttsai.dto.request.RequestDto;
import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.audiofile.AudioFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AlertMapper {

    AlertDto mapToDto(Alert alert);
    List<AlertDto> mapToDto(List<Alert> alert);
    Alert mapToEntity(AlertDto alertDto);

    @Mapping(target = "fileType", source = "audioFile.fileType")
    @Mapping(target = "audioFile", source = "audioFile.audioFile")
    AlertModelApi mapModelApiToDto(AlertDto alertDto);

    List<AlertModelApi> mapToModelApi(List<AlertDto> alert);

    @Mapping(target = "audioFile", ignore = true)
    AlertDto mapDtoToModelApi(AlertModelApi alertModelApi);

    AlertPageModelApi mapPageToModelApi(AlertPage alertPage);
}
