CREATE TABLE IF NOT EXISTS VOICE(
    ID BIGSERIAL PRIMARY KEY,
    VOICE_NAME VARCHAR(1000),
    VOICE_ID VARCHAR(300),
    CATEGORY VARCHAR(300)
);