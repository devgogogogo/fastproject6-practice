package com.fastcampus.crash.exception.sessionspeaker;

import com.fastcampus.crash.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class SessionSpeakerNotFoundException extends ClientErrorException {

    public SessionSpeakerNotFoundException() {
        super(HttpStatus.NOT_FOUND,"발표자를 찾을 수 없습니다.");
    }

    public SessionSpeakerNotFoundException(Long speakerId) {
        super(HttpStatus.NOT_FOUND, "SessionSpeaker with speakerId " + speakerId + " not found");
    }

}
