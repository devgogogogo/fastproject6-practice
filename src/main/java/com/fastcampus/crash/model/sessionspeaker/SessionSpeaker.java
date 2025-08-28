package com.fastcampus.crash.model.sessionspeaker;

import com.fastcampus.crash.model.entity.SessionSpeakerEntity;

public record SessionSpeaker(Long speakerId, String company, String name, String description, String profile) {


    public static SessionSpeaker from(SessionSpeakerEntity entity) {
        return new SessionSpeaker(
                entity.getSpeakerId(),
                entity.getCompany(),
                entity.getName(),
                entity.getDescription(),
                entity.getProfile());
    }
}
