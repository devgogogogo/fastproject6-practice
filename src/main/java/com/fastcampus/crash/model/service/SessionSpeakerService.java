package com.fastcampus.crash.model.service;

import com.fastcampus.crash.exception.sessionspeaker.SessionSpeakerNotFoundException;
import com.fastcampus.crash.model.entity.SessionSpeakerEntity;
import com.fastcampus.crash.model.repository.SessionSpeakerEntityRepository;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeaker;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionSpeakerService {

    private final SessionSpeakerEntityRepository sessionSpeakerEntityRepository;

    //SessionSpeakerEntity를 찾는 메서드인데 반복적으로 사용하다보니 따로 빼서 만드러줬다.
    public SessionSpeakerEntity getSessionSpeakerEntityBySpeakerId(Long speakerId) {
       return sessionSpeakerEntityRepository.findById(speakerId).orElseThrow(SessionSpeakerNotFoundException::new);
    }

    public List<SessionSpeaker> getSessionSpeakers() {
        List<SessionSpeakerEntity> sessionSpeakerEntities = sessionSpeakerEntityRepository.findAll();
        List<SessionSpeaker> sessionSpeakerList = sessionSpeakerEntities.stream().map(entity -> SessionSpeaker.from(entity)).toList();
        return sessionSpeakerList;
    }

    public SessionSpeaker getSessionSpeakerBySpeakerId(Long speakersId) {
        SessionSpeakerEntity sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakersId);
       return SessionSpeaker.from(sessionSpeakerEntity);
    }

    public SessionSpeaker createdSessionSpeaker(SessionSpeakerPostRequestBody request) {
        SessionSpeakerEntity sessionSpeakerEntity = SessionSpeakerEntity.of(request.company(), request.name(), request.description());
        SessionSpeakerEntity saved = sessionSpeakerEntityRepository.save(sessionSpeakerEntity);
       return SessionSpeaker.from(saved);
    }

    public SessionSpeaker updateSessionSpeaker(Long speakersId, SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody) {
        SessionSpeakerEntity sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakersId); //DB에 엔티티 먼저 찾고

        if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.company())) { //값이 존재하면 수정해준다.
            sessionSpeakerEntity.setCompany(sessionSpeakerPatchRequestBody.company());
        }

        if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.name())) { //값이 존재하면 수정해준다.
            sessionSpeakerEntity.setName(sessionSpeakerPatchRequestBody.name());
        }

        if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.description())) { //값이 존재하면 수정해준다.
            sessionSpeakerEntity.setDescription(sessionSpeakerPatchRequestBody.description());
        }

        SessionSpeakerEntity saved = sessionSpeakerEntityRepository.save(sessionSpeakerEntity);
        return SessionSpeaker.from(saved);
    }

    public void deleteSessionSpeaker(Long speakersId) {
        SessionSpeakerEntity sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakersId);
        sessionSpeakerEntityRepository.delete(sessionSpeakerEntity);
    }
}
