package com.fastcampus.crash.model.service;

import com.fastcampus.crash.exception.registration.RegistrationAlreadyExistsException;
import com.fastcampus.crash.exception.registration.RegistrationNotFoundException;
import com.fastcampus.crash.model.crashsession.CrashSessionRegistrationStatus;
import com.fastcampus.crash.model.entity.CrashSessionEntity;
import com.fastcampus.crash.model.entity.RegistrationEntity;
import com.fastcampus.crash.model.entity.UserEntity;
import com.fastcampus.crash.model.registration.Registration;
import com.fastcampus.crash.model.registration.RegistrationPostRequestBody;
import com.fastcampus.crash.model.repository.RegistrationEntityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationEntityRepository registrationEntityRepository;
    private final CrashSessionService crashSessionService;

    //반복적인 메서드를 따로 빼서 만듬
    public RegistrationEntity getRegistrationEntityByRegistrationIdAndUserEntity(Long registrationId, UserEntity userEntity) {
        return registrationEntityRepository.findByRegistrationIdAndUser(registrationId, userEntity).orElseThrow(() -> new RegistrationNotFoundException());
    }

    public List<Registration> getRegistrationsByCurrentUser(UserEntity currentUser) {
        List<RegistrationEntity> registrationEntities = registrationEntityRepository.findByUser(currentUser);
        return registrationEntities.stream().map(Registration::from).toList();
    }

    public Registration getRegistrationByRegistrationIdByCurrentUser(Long registrationId, UserEntity currentUser) {
        RegistrationEntity registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, currentUser);
        return Registration.from(registrationEntity);
    }

    public Registration createRegistrationByCurrentUser(@Valid RegistrationPostRequestBody registrationPostRequestBody, UserEntity currentUser) {
        CrashSessionEntity crashSessionEntity = crashSessionService.getCrashSessionEntityBySessionId(registrationPostRequestBody.sessionId());
        registrationEntityRepository.findByUserAndSession(currentUser, crashSessionEntity) //등록 중족있는지 체크한다. 있다면 예외처리함
                .ifPresent(registrationEntity -> {throw new RegistrationAlreadyExistsException(registrationEntity.getRegistrationId(), currentUser);});
        RegistrationEntity registrationEntity = RegistrationEntity.of(currentUser, crashSessionEntity);
        RegistrationEntity saved = registrationEntityRepository.save(registrationEntity);
        return Registration.from(saved);
    }

    public void deleteRegistrationByRegistrationIdAndByCurrentUser(Long registrationId, UserEntity principal) {
        RegistrationEntity registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, principal);
        registrationEntityRepository.delete(registrationEntity);
    }

    public CrashSessionRegistrationStatus getCrashSessionRegistrationstatusBySessionId(Long sessionId, UserEntity currentUser) {

        CrashSessionEntity crashSessionEntity = crashSessionService.getCrashSessionEntityBySessionId(sessionId);
        Optional<RegistrationEntity> registrationEntity = registrationEntityRepository.findByUserAndSession(currentUser, crashSessionEntity);

       return new CrashSessionRegistrationStatus(
                sessionId,
                registrationEntity.isPresent(),
                registrationEntity.map(RegistrationEntity::getRegistrationId).orElse(null));
    }
}
