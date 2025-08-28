package com.fastcampus.crash.model.entity;

import com.fastcampus.crash.model.crashsession.CrashSessionCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "crashsession")
@NoArgsConstructor
@Getter
@Setter
public class CrashSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CrashSessionCategory category;

    @Column(nullable = false)
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "speakerid")
    private SessionSpeakerEntity speaker;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CrashSessionEntity that)) return false;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(title, that.title) && Objects.equals(body, that.body) && category == that.category && Objects.equals(dateTime, that.dateTime) && Objects.equals(speaker, that.speaker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, title, body, category, dateTime, speaker);
    }

    public static CrashSessionEntity of(String title, String body, CrashSessionCategory crashSessionCategory, ZonedDateTime dateTime, SessionSpeakerEntity sessionSpeakerEntity) {
        CrashSessionEntity crashSessionEntity = new CrashSessionEntity();
        crashSessionEntity.setTitle(title);
        crashSessionEntity.setBody(body);
        crashSessionEntity.setCategory(crashSessionCategory);
        crashSessionEntity.setDateTime(dateTime);
        crashSessionEntity.setSpeaker(sessionSpeakerEntity);
        return crashSessionEntity;
    }
}
