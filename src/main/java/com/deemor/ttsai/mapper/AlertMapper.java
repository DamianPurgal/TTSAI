package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.AlertEventModelApi;
import com.deemor.TTSAI.model.AlertModelApi;
import com.deemor.TTSAI.model.AlertPageModelApi;
import com.deemor.ttsai.dto.alert.AlertDto;
import com.deemor.ttsai.dto.alert.AlertPage;
import com.deemor.ttsai.dto.event.EventDto;
import com.deemor.ttsai.entity.alert.Alert;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AlertMapper {

    @Mapping(target = "events", source = "alert", qualifiedByName = "alertToEventsDto")
    AlertDto mapToDto(Alert alert);

    List<AlertDto> mapToDto(List<Alert> alert);

    Alert mapToEntity(AlertDto alertDto);

    @Mapping(target = "events", source = "events", qualifiedByName = "dtoToModelApi")
    AlertModelApi mapModelApiToDto(AlertDto alertDto);

    List<AlertModelApi> mapToModelApi(List<AlertDto> alert);


    AlertPageModelApi mapPageToModelApi(AlertPage alertPage);

    @Named("alertToEventsDto")
    default List<EventDto> getEventDtoFromAlert(Alert alert) {
        List<EventDto> events = new ArrayList<>();
        alert.getAlertEvents().forEach(element -> {
            EventDto eventDto = new EventDto();
            eventDto.setAudioFile(element.getAudioFile().getAudioFile());
            eventDto.setOrderIndex(element.getOrderIndex());
            eventDto.setFileType(element.getAudioFile().getFileType());
            eventDto.setVoiceType(element.getAudioFile().getVoiceType());
            events.add(eventDto);
        });
        return events;
    }

    @Named("dtoToModelApi")
    default AlertEventModelApi dtoToModelApi(EventDto eventDto) {
        AlertEventModelApi alertEventModelApi = new AlertEventModelApi();
        alertEventModelApi.audioFile(eventDto.getAudioFile());
        alertEventModelApi.fileType(eventDto.getFileType());
        alertEventModelApi.orderIndex(eventDto.getOrderIndex());
        alertEventModelApi.setVoiceType(eventDto.getVoiceType());

        return alertEventModelApi;
    }
}
