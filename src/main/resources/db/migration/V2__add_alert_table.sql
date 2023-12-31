CREATE TABLE IF NOT EXISTS ALERT(
    ID BIGSERIAL PRIMARY KEY,
    MESSAGE VARCHAR(300),
    VOICE_TYPE VARCHAR(100),
    REQUESTER VARCHAR(100),
    SUBMITTER VARCHAR(100),
    STATUS VARCHAR(100),
    DATE_OF_CREATION TIMESTAMP,
    AUDIO_FILE_ID BIGINT
);

CREATE TABLE IF NOT EXISTS AUDIO_FILE(
    ID BIGSERIAL PRIMARY KEY,
    FILE_TYPE VARCHAR(200),
    FILE BYTEA
);