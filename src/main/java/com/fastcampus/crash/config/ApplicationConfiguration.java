package com.fastcampus.crash.config;


import com.fastcampus.crash.model.service.SessionSpeakerService;
import com.fastcampus.crash.model.service.UserService;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeaker;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.fastcampus.crash.model.user.UserSignUpRequestBody;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

   private static final Faker faker = new Faker();

   private final UserService userService;
   private final SessionSpeakerService sessionSpeakerService;

    @Bean
    public ApplicationRunner applicationRunner() {


        //어플리케이션을 구동시키고 나서 동작시키고 싶은 다양한 로직들을 작성하기만 하면 밑에 있는 오직들이 실행됨
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                //TODO: 유저 및 세션 스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);
            }
        };
    }

    private void createTestUsers() {
        userService.signUp(new UserSignUpRequestBody("jayce", "1234", "Dev Jayce", "jayce@crash.com"));//이렇게 작성할수있고
        userService.signUp(new UserSignUpRequestBody("jay", "1234", "Dev jay", "jay@crash.com"));
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(), faker.internet().emailAddress())); // 이런식으로 이용할수도 있다.
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(), faker.internet().emailAddress()));
    }

    private void createTestSessionSpeakers(int numberOfSpeakers) {
        IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker()).toList();
    }

    private SessionSpeaker createTestSessionSpeaker() {
        String name = faker.name().fullName();
        String company = faker.company().name();
        String description = faker.shakespeare().romeoAndJulietQuote();
        return sessionSpeakerService.createdSessionSpeaker(new SessionSpeakerPostRequestBody(company, name, description));
    }
}
