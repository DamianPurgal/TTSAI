package com.deemor.ttsai.dto.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private byte[] audioFile;
    private String fileType;
    private String voiceType;
    private Long orderIndex;

}
