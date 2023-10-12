package com.deemor.ttsai.db.migration;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class V8__event_migration extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT id, AUDIO_FILE_ID FROM ALERT")) {
                while (rows.next()) {
                    long alertId = rows.getLong(1);
                    long audioFileId = rows.getLong(2);

                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute(
                                String.format(
                                        "INSERT INTO ALERT_EVENT (ALERT_ID, AUDIO_FILE_ID, ORDER_INDEX) VALUES (%d, %d, %d)",
                                        alertId,
                                        audioFileId,
                                        0
                                )
                        );
                    }
                }
            }
        }
    }



}
