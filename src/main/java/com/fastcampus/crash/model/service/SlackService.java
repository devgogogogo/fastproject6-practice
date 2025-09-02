package com.fastcampus.crash.model.service;

import com.fastcampus.crash.model.registration.Registration;
import com.fastcampus.crash.model.slack.SlackNotificationBlock;
import com.fastcampus.crash.model.slack.SlackNotificationMessage;
import com.fastcampus.crash.model.slack.SlackNotificationText;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {

    private static final RestClient restClient = RestClient.create();

    public void sendSlackNotification(Registration registration) {
        String linkText = getRegistrationPageLinkText(registration);

        SlackNotificationMessage slackNotificationMessage =
                new SlackNotificationMessage(List.of(
                        new SlackNotificationBlock("section",
                                new SlackNotificationText("mrkdwn", linkText))));

        String response = restClient
                .post()
                .uri(
                        "https://hooks.slack.com/services/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")
                .body(slackNotificationMessage)
                .retrieve()
                .body(String.class);

        log.info(response);
    }

    private String getRegistrationPageLinkText(Registration registration) {
        String baseLink = "https://dev-jayce.github.io/crash/registration.html?registration=";
        Long registrationId = registration.registrationId();
        String username = registration.user().username();
        Long sessionId = registration.session().sessionId();
        String link = baseLink + registrationId + "," + username + "," + sessionId;
        return ":collision: *CRASH* <" + link + "|Registration Details>";
    }
}
