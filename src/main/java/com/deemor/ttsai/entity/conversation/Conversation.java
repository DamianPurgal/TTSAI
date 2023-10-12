package com.deemor.ttsai.entity.conversation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation implements Serializable {

    private Integer orderIndex;
    private String voiceType;
    private String message;
}
