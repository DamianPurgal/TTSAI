package com.deemor.ttsai.entity.alert;

import com.deemor.ttsai.converter.ConversationListConverter;
import com.deemor.ttsai.entity.conversation.Conversation;
import com.deemor.ttsai.entity.alertevent.AlertEvent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALERT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

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
    private AlertStatus alertStatus;

    @Column(name = "DATE_OF_CREATION")
    private LocalDateTime dateOfCreation;

    @OneToMany(mappedBy="alert", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AlertEvent> alertEvents = new ArrayList<>();

    @Column(name = "CONVERSATION")
    @Convert(converter = ConversationListConverter.class)
    private List<Conversation> conversation = new ArrayList<>();

}