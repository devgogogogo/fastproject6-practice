package com.fastcampus.crash.model.entity;

import com.fastcampus.crash.model.user.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;


//발표자
@Entity
@Table(name = "sessionspeaker")
@NoArgsConstructor
@Getter
@Setter
public class SessionSpeakerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speakerId;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String name; // 발표자이름

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; //자기소개

    @Column(nullable = false)
    private String profile; // 프로필

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SessionSpeakerEntity that)) return false;
        return Objects.equals(speakerId, that.speakerId) && Objects.equals(company, that.company) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speakerId, company, name, description, profile);
    }

    public static SessionSpeakerEntity of(String company, String name, String description) {
        var sessionSpeakerEntity = new SessionSpeakerEntity();
        sessionSpeakerEntity.setCompany(company);
        sessionSpeakerEntity.setName(name);
        sessionSpeakerEntity.setDescription(description);
        sessionSpeakerEntity.setProfile(
                "https://dev-jayce.github.io/public/profile/" + (new Random().nextInt(100) + 1) + ".png");
        return sessionSpeakerEntity;
    }
}
