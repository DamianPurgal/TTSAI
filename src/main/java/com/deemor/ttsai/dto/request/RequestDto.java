package com.deemor.ttsai.dto.request;

import com.deemor.ttsai.entity.request.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private String message;
    private String voiceType;
    private String requester;
    private String submitter;
    private RequestStatus requestStatus;
    private LocalDateTime dateOfCreation;
}
