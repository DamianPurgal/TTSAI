ALTER TABLE ALERT ADD CONVERSATION varchar(5000);
ALTER TABLE REQUEST ADD CONVERSATION varchar(5000);
ALTER TABLE AUDIO_FILE ADD MESSAGE varchar(2000);
ALTER TABLE AUDIO_FILE ADD VOICE_TYPE varchar(100);