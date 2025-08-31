package com.fastcampus.crash.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "registration",
        indexes = {
                @Index(name = "registration_userid_sessionid_idx", columnList = "userid, sessionid", unique = true)
        })
@Setter
@Getter
@NoArgsConstructor
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "sessionid")
    private CrashSessionEntity session;

    @Column
    private ZonedDateTime createDateTime;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegistrationEntity that)) return false;
        return Objects.equals(registrationId, that.registrationId) && Objects.equals(user, that.user) && Objects.equals(session, that.session) && Objects.equals(createDateTime, that.createDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationId, user, session, createDateTime);
    }

    public static RegistrationEntity of(UserEntity userEntity, CrashSessionEntity sessionEntity) {
        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setUser(userEntity);
        registrationEntity.setSession(sessionEntity);
        return registrationEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createDateTime = ZonedDateTime.now();
    }
}
