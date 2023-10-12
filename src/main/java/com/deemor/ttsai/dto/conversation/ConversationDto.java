package com.deemor.ttsai.dto.conversation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversationDto {

    private Integer orderIndex;
    private String voiceType;
    private String message;
}
