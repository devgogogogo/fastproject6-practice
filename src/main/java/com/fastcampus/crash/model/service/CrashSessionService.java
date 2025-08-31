package com.fastcampus.crash.model.service;

import com.fastcampus.crash.exception.crashsession.CrashSessionNotFoundException;
import com.fastcampus.crash.model.crashsession.CrashSession;
import com.fastcampus.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.fastcampus.crash.model.crashsession.CrashSessionPostRequestBody;
import com.fastcampus.crash.model.entity.CrashSessionEntity;
import com.fastcampus.crash.model.entity.SessionSpeakerEntity;
import com.fastcampus.crash.model.repository.CrashSessionEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrashSessionService {

    private final CrashSessionEntityRepository crashSessionEntityRepository;
    private final SessionSpeakerService sessionSpeakerService;


    //중복 메서드를 따로 뺴줘서 만들어줬음
    public CrashSessionEntity getCrashSessionEntityBySessionId(Long sessionId) {
        return crashSessionEntityRepository.findById(sessionId).orElseThrow(() -> new CrashSessionNotFoundException(sessionId));
    }

    //전체조회
    public List<CrashSession> getCrashSessions() {
        List<CrashSession> list = crashSessionEntityRepository.findAll().stream().map(CrashSession::from).toList();
        return list;
    }

    //단건 조회
    public CrashSession getCrashSessionBySessionId(Long sessionId) {
        CrashSessionEntity crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
        return CrashSession.from(crashSessionEntity);
    }

    public CrashSession createCrashSession(CrashSessionPostRequestBody crashSessionPostRequestBody) {
        SessionSpeakerEntity sessionSpeakerEntity = sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(crashSessionPostRequestBody.speakerId()); //sessionSpeakerService를 활용해서 sessionSpeakerEntity를 불러온다.

        CrashSessionEntity crashSessionEntity = CrashSessionEntity.of( //요청한crashSessionPostRequestBody를 crashSessionEntity 객체에 넣어준다.
                crashSessionPostRequestBody.title(),
                crashSessionPostRequestBody.body(),
                crashSessionPostRequestBody.category(),
                crashSessionPostRequestBody.dateTime(),
                sessionSpeakerEntity);

        CrashSessionEntity saved = crashSessionEntityRepository.save(crashSessionEntity);
       return CrashSession.from(saved);
    }

    public CrashSession updateCrashSession(Long sessionId, CrashSessionPatchRequestBody crashSessionPatchRequestBody) {

        CrashSessionEntity crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.title())) { //요청한 파라미터가 수정할 값이 존재하는지(값이 있으면) 수정하는 로직을 구현한다.
            crashSessionEntity.setTitle(crashSessionPatchRequestBody.title());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.body())) { //요청한 파라미터가 수정할 값이 존재하는지(값이 있으면) 수정하는 로직을 구현한다.
            crashSessionEntity.setBody(crashSessionPatchRequestBody.body());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.category())) { //요청한 파라미터가 수정할 값이 존재하는지(값이 있으면) 수정하는 로직을 구현한다.
            crashSessionEntity.setCategory(crashSessionPatchRequestBody.category());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.dateTime())) { //요청한 파라미터가 수정할 값이 존재하는지(값이 있으면) 수정하는 로직을 구현한다.
            crashSessionEntity.setDateTime(crashSessionPatchRequestBody.dateTime());
        }
        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.speakerId())) { //요청한 파라미터가 수정할 값이 존재하는지(값이 있으면) 수정하는 로직을 구현한다.
            SessionSpeakerEntity sessionSpeakerEntity = sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(crashSessionPatchRequestBody.speakerId());
            crashSessionEntity.setSpeaker(sessionSpeakerEntity);
        }
        CrashSessionEntity saved = crashSessionEntityRepository.save(crashSessionEntity);
        return CrashSession.from(saved);
    }

    public void deleteCrashSession(Long sessionId) {
        CrashSessionEntity crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
        crashSessionEntityRepository.delete(crashSessionEntity);
    }
}
