package com.deemor.ttsai.db.migration;

import com.deemor.ttsai.entity.conversation.Conversation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class V5__convert_message_to_conversation extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        updateAudioFileTable(context);
        updateRequestTable(context);
        updateAlertTable(context);
    }

    private void updateAudioFileTable(Context context) throws Exception {
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT id, message, voice_type, AUDIO_FILE_ID FROM ALERT")) {
                while (rows.next()) {
                    long requestId = rows.getLong(1);
                    String message = rows.getString(2).replace("'", "");
                    String voiceType = rows.getString(3);
                    long audioFileId = rows.getLong(4);


                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute(
                                String.format(
                                        "UPDATE AUDIO_FILE SET MESSAGE='%s' , VOICE_TYPE='%s' WHERE ID=%d",
                                        message,
                                        voiceType,
                                        audioFileId
                                )
                        );
                    }
                }
            }
        }
    }

    private void updateRequestTable(Context context) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT id, message, voice_type FROM REQUEST")) {
                while (rows.next()) {
                    long requestId = rows.getLong(1);
                    String message = rows.getString(2).replace("'", "");
                    String voiceType = rows.getString(3);

                    Conversation conversation = new Conversation(0, voiceType, message);
                    String conversationJson = objectMapper.writeValueAsString(List.of(conversation));

                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute(
                                String.format(
                                        "UPDATE REQUEST SET conversation='%s' WHERE ID=%d",
                                        conversationJson,
                                        requestId
                                )
                        );
                    }
                }
            }
        }
    }

    private void updateAlertTable(Context context) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT id, message, voice_type FROM ALERT")) {
                while (rows.next()) {
                    long requestId = rows.getLong(1);
                    String message = rows.getString(2).replace("'", "");
                    String voiceType = rows.getString(3);

                    Conversation conversation = new Conversation(0, voiceType, message);
                    String conversationJson = objectMapper.writeValueAsString(List.of(conversation));

                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute(
                                String.format(
                                        "UPDATE ALERT SET conversation='%s' WHERE ID=%d",
                                        conversationJson,
                                        requestId
                                )
                        );
                    }
                }
            }
        }
    }

}
