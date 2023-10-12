package com.deemor.ttsai.entity.request;

import com.deemor.ttsai.converter.ConversationListConverter;
import com.deemor.ttsai.entity.conversation.Conversation;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "REQUEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "REQUESTER")
    private String requester;

    @Column(name = "SUBMITTER")
    private String submitter;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RequestStatus requestStatus;

    @Column(name = "DATE_OF_CREATION")
    private LocalDateTime dateOfCreation;

    @Column(name = "CONVERSATION")
    @Convert(converter = ConversationListConverter.class)
    private List<Conversation> conversation;

}
