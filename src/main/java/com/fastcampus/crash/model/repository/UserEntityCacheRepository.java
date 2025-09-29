package com.fastcampus.crash.model.repository;


import com.fastcampus.crash.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserEntityCacheRepository {

    private final RedisTemplate<String, UserEntity> userEntityRedisTemplate;

    public void setUserEntityCache(UserEntity userEntity) {
        String redisKey = getRedisKey(userEntity.getUsername());
        userEntityRedisTemplate.opsForValue().set(redisKey, userEntity, Duration.ofSeconds(30));
    }

    public Optional<UserEntity> getUserEntityCache(String username) {
        String redisKey = getRedisKey(username);
        UserEntity userEntity = userEntityRedisTemplate.opsForValue().get(redisKey);

        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userEntity);
        return optionalUserEntity;
    }

    private String getRedisKey(String username) {
        return "user:" + username;
    }
}
