package com.fastcampus.crash.model.repository;


import com.fastcampus.crash.model.crashsession.CrashSession;
import com.fastcampus.crash.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CrashSessionCacheRepository {

    private final RedisTemplate<String, CrashSession> carashSessionRedisTemplate;
    private final RedisTemplate<String, List<CrashSession>> crashSessionsListRedisTemplate;

    public void setCrashSessionCache(CrashSession crashSession) {
        String redisKey = getRedisKey(crashSession.sessionId());
        carashSessionRedisTemplate.opsForValue().set(redisKey, crashSession);
    }

    public void setCrashSessionsListCache(List<CrashSession> crashSession) {
        crashSessionsListRedisTemplate.opsForValue().set("sessions", crashSession);
    }

    public Optional<CrashSession> getCrashSessionCache(Long sessionId) {
        String redisKey = getRedisKey(sessionId);
        CrashSession crashSession = carashSessionRedisTemplate.opsForValue().get(redisKey);

        Optional<CrashSession> optionalUserEntity = Optional.ofNullable(crashSession);
        return optionalUserEntity;
    }

    public List<CrashSession> getCrashSessionsListCache() {
        List<CrashSession> crashSessions = crashSessionsListRedisTemplate.opsForValue().get("sessions");

        if (ObjectUtils.isEmpty(crashSessions)) {
            return Collections.emptyList();
        }
        return crashSessions;
    }

    private String getRedisKey(Long sessionId) {
        return "session:" + sessionId;
    }
}
