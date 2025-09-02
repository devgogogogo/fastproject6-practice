package com.fastcampus.crash.config;


import com.fastcampus.crash.model.coinbase.PriceResponse;
import com.fastcampus.crash.model.crashsession.CrashSessionCategory;
import com.fastcampus.crash.model.crashsession.CrashSessionPostRequestBody;
import com.fastcampus.crash.model.exchange.ExchangeResponse;
import com.fastcampus.crash.model.service.CrashSessionService;
import com.fastcampus.crash.model.service.SessionSpeakerService;
import com.fastcampus.crash.model.service.SlackService;
import com.fastcampus.crash.model.service.UserService;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeaker;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.fastcampus.crash.model.user.UserSignUpRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfiguration {

    private static final RestClient restClient = RestClient.create();
    private static final Faker faker = new Faker();

    private final UserService userService;
    private final SessionSpeakerService sessionSpeakerService;
    private final CrashSessionService crashSessionService;

    private final SlackService slackService;

    @Bean
    public ApplicationRunner applicationRunner() {

        //어플리케이션을 구동시키고 나서 동작시키고 싶은 다양한 로직들을 작성하기만 하면 밑에 있는 오직들이 실행됨
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {

                //TODO: 유저 및 세션 스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);
//
//                //TODO : Bitcoin USD 가격 조회
//                Double bitcoinUsdPrice = getBitcoinUsdPrice();
//
//                //TODO: Usd to KRW 환율 조회
//                Double usdToKrwExchangeRate = getUsdToKrwExchangeRate();
//
//                //TODO:  Bitcoin KRW 가격 계산
//                double koreanPremium = 1.1;
//                double bitcoinKrwPrice = bitcoinUsdPrice * usdToKrwExchangeRate * koreanPremium;
//                log.info(String.format("BTC KRW: %.2f",bitcoinKrwPrice));
            }
        };
    }

    private Double getBitcoinUsdPrice() {
        PriceResponse response = restClient
                .get()
                .uri("https://api.coinbase.com/v2/prices/BTC-USD/buy")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (requ, resp) -> {
                    //TODO: 클라이언트 에러 예외 처리를 커스텀하게 하면 좋다. 근데 강의니깐 간단하게 로깅으로만 남긴다.
                    log.error(new String(resp.getBody().readAllBytes(), StandardCharsets.UTF_8));
                })
                .body(PriceResponse.class);
        assert response != null;
        log.info(response.toString());

        return Double.parseDouble(response.data().amount());
    }

    //    https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=3sMvoIaYqWE17iCw8v0fy2IxB7LemZaK&searchdate=20180102&data=AP01
    private Double getUsdToKrwExchangeRate() {
        ExchangeResponse[] response = restClient
                .get()
                .uri("https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=여기에는 인증키를 넣어야함&searchdate=20180102&data=AP01")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (requ, resp) -> {
                    //TODO: 클라이언트 에러 예외 처리를 커스텀하게 하면 좋다. 근데 강의니깐 간단하게 로깅으로만 남긴다.
                    log.error(new String(resp.getBody().readAllBytes(), StandardCharsets.UTF_8));
                })
                .body(ExchangeResponse[].class);
        assert response != null;
        log.info(response.toString());
        ExchangeResponse usdToKrwExchangeRate = Arrays.stream(response)
                .filter(exchangeResponse -> exchangeResponse.cur_unit().equals("USD"))
                .findFirst()
                .orElseThrow();
        return Double.parseDouble(usdToKrwExchangeRate.deal_bas_r().replace(",", ""));
    }

    private void createTestUsers() {
        userService.signUp(new UserSignUpRequestBody("jayce", "1234", "Dev Jayce", "jayce@crash.com"));//이렇게 작성할수있고
        userService.signUp(new UserSignUpRequestBody("jay", "1234", "Dev jay", "jay@crash.com"));
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(), faker.internet().emailAddress())); // 이런식으로 이용할수도 있다.
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(), faker.internet().emailAddress()));
    }

    private void createTestSessionSpeakers(int numberOfSpeakers) {

        List<SessionSpeaker> sessionSpeakers = IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker()).toList();

        sessionSpeakers.forEach(
                sessionSpeaker -> {
                    int numberOfSessions = new Random().nextInt(4) + 1;
                    IntStream.range(0, numberOfSessions).forEach(i -> createTestCrashSession(sessionSpeaker));
                });

    }

    private SessionSpeaker createTestSessionSpeaker() {
        String name = faker.name().fullName();
        String company = faker.company().name();
        String description = faker.shakespeare().romeoAndJulietQuote();
        return sessionSpeakerService.createdSessionSpeaker(new SessionSpeakerPostRequestBody(company, name, description));
    }

    private void createTestCrashSession(SessionSpeaker sessionSpeaker) {
        String title = faker.book().title();


        String body = faker.shakespeare().asYouLikeItQuote()
                + faker.shakespeare().hamletQuote()
                + faker.shakespeare().kingRichardIIIQuote()
                + faker.shakespeare().romeoAndJulietQuote();

        crashSessionService.createCrashSession(
                new CrashSessionPostRequestBody(
                        title,
                        body,
                        getRandomCategory(),
                        ZonedDateTime.now().plusDays(new Random().nextInt(2) + 1),
                        sessionSpeaker.speakerId()));
    }

    private CrashSessionCategory getRandomCategory() {

        CrashSessionCategory[] categories = CrashSessionCategory.values();
        int randomIndex = new Random().nextInt(categories.length);
        return categories[randomIndex];
    }
}
