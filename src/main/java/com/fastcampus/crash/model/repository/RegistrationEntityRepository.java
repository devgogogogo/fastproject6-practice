package com.fastcampus.crash.model.repository;

import com.fastcampus.crash.model.entity.CrashSessionEntity;
import com.fastcampus.crash.model.entity.RegistrationEntity;
import com.fastcampus.crash.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationEntityRepository extends JpaRepository<RegistrationEntity, Long> {

    List<RegistrationEntity> findByUser(UserEntity user);

    Optional<RegistrationEntity> findByRegistrationIdAndUser(Long registrationId, UserEntity user);

    //이미 신청한적이 있거나 중복으로 생성하지 못하게끔 하려고 사전에 먼저 조회를 한다.
    Optional<RegistrationEntity> findByUserAndSession(UserEntity user, CrashSessionEntity session);
}
