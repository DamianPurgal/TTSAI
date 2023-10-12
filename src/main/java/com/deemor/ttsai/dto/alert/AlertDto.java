package com.deemor.ttsai.dto.alert;

import com.deemor.ttsai.dto.conversation.ConversationDto;
import com.deemor.ttsai.dto.event.EventDto;
import com.deemor.ttsai.entity.alert.AlertStatus;
import com.deemor.ttsai.entity.audiofile.AudioFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AlertDto {

    private Long id;
    private String message;
    private String voiceType;
    private String requester;
    private String submitter;
    private AlertStatus alertStatus;
    private LocalDateTime dateOfCreation;
    private AudioFile audioFile;
    private List<ConversationDto> conversation;
    private List<EventDto> events;
}