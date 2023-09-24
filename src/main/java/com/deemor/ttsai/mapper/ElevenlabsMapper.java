package com.deemor.ttsai.mapper;

import com.deemor.TTSAI.model.VoiceModelApi;
import com.deemor.ttsai.dto.VoiceDto;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ElevenlabsMapper {

    VoiceDto mapVoiceToDto(Voice voice);
    List<VoiceDto> mapListVoiceToDto(List<Voice> voice);

    VoiceModelApi mapVoiceDtoToModelApi(VoiceDto voice);
    List<VoiceModelApi> mapListVoiceDtoToModelApi(List<VoiceDto> voice);
}
