package com.fastcampus.crash.model.service;

import com.fastcampus.crash.exception.user.UserAlreadyExistsException;
import com.fastcampus.crash.exception.user.UserNotFoundException;
import com.fastcampus.crash.model.entity.UserEntity;
import com.fastcampus.crash.model.repository.UserEntityCacheRepository;
import com.fastcampus.crash.model.repository.UserEntityRepository;
import com.fastcampus.crash.model.user.User;
import com.fastcampus.crash.model.user.UserAuthenticationResponse;
import com.fastcampus.crash.model.user.UserLoginRequestBody;
import com.fastcampus.crash.model.user.UserSignUpRequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserEntityCacheRepository userEntityCacheRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = getUserEntityByUsername(username);
        return userEntity;
    }

    public User signUp(@Valid UserSignUpRequestBody userSignUpRequestBody) {

        userEntityRepository.findByUsername(userSignUpRequestBody.username()).ifPresent(user -> {throw new UserAlreadyExistsException();}); //존재하는지 찾아본다음

        //엔티티객체에 넣어주고
        UserEntity userEntity = UserEntity.of(
                userSignUpRequestBody.username(),
                bCryptPasswordEncoder.encode(userSignUpRequestBody.password()),
                userSignUpRequestBody.name(),
                userSignUpRequestBody.email());

        //DB에 저장한다.
        UserEntity saved = userEntityRepository.save(userEntity);
        User user = User.from(saved);
        return user;
    }

    public UserAuthenticationResponse login(@Valid UserLoginRequestBody userLoginRequestBody) {
        UserEntity userEntity = getUserEntityByUsername(userLoginRequestBody.username());
        if (passwordEncoder.matches(userLoginRequestBody.password(), userEntity.getPassword())) {
            String accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }
    }

    //유저네임을 전달받아서 유저 엔티티를 검색해주고 이때 해당유저 네임을 가진 유저 엔티티가 없으면 userNotFoundException을 날려주는 메소드를 만들어준다.(반복적으로 사용할것이기 때문에)
    private UserEntity getUserEntityByUsername(String username) {
        Optional<UserEntity> userEntityCache = userEntityCacheRepository.getUserEntityCache(username);

        if (userEntityCache.isPresent()) {
            UserEntity userEntity = userEntityCache.get();
            return userEntity;
        } else {
            UserEntity userEntity = userEntityRepository
                    .findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

            userEntityCacheRepository.setUserEntityCache(userEntity);

            return userEntity;
        }
    }
}
