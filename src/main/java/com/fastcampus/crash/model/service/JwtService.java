package com.fastcampus.crash.model.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtService {
    private final SecretKey key;

    public JwtService(@Value("${jwt.secret-key}") String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String getUsername(String accessToken) {
        return getSubject(accessToken);
    }

    private String generateToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + (1000 * 60 * 60 * 3)); // 1초 * 60(초) *60(분) * 3 ==> 3시간

        String token = Jwts.builder()
                .subject(subject)
                .signWith(key)
                .issuedAt(now)
                .expiration(exp)
                .compact();
        return token;
    }

    //subject를 추출하는 함수
    //jwt의 시그니처도 검증하고 유효기간도 검증하고, 다양한 검증이 이루어짐
    // 이 검증 과정에서 검증에 실패하면 jwtException이 터짐
    private String getSubject(String token) {
        try {
            String subject = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return subject;
        } catch (JwtException e) {
            log.error("JwtException",e);
           throw e;
        }
    }
}
