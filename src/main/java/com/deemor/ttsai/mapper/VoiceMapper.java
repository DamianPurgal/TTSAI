package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.VoiceModelApi;
import com.deemor.ttsai.dto.VoiceDto;
import com.deemor.ttsai.entity.voice.AiVoice;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VoiceMapper {

    VoiceDto mapToDto(AiVoice voice);
    List<VoiceDto> mapToDto(List<AiVoice> voice);

    AiVoice mapToEntity(VoiceDto voice);
    List<AiVoice> mapToEntity(List<VoiceDto> voice);

    VoiceModelApi mapVoiceDtoToModelApi(VoiceDto voice);
    List<VoiceModelApi> mapListVoiceDtoToModelApi(List<VoiceDto> voice);
}
