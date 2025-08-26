package com.fastcampus.crash.model.repository;

import com.fastcampus.crash.exception.sessionspeaker.SessionSpeakerNotFoundException;
import com.fastcampus.crash.model.entity.SessionSpeakerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionSpeakerEntityRepository extends JpaRepository<SessionSpeakerEntity, Long> {


//    //이런식으로 optional을 처리 할수 있다.
//    Optional<SessionSpeakerEntity> findBySpeakerId(Long speakerId);
//
//    default SessionSpeakerEntity findBySpeakerIdOrElseThrow(Long speakerId) {
//        return findBySpeakerId(speakerId).orElseThrow(SessionSpeakerNotFoundException::new);
//    }
}
