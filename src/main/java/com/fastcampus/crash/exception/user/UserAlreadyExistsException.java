package com.fastcampus.crash.exception.user;

import com.fastcampus.crash.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ClientErrorException {

    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT,"유저가 이미 존재합니다.");
    }

    public UserAlreadyExistsException(String username) {
        super(HttpStatus.NOT_FOUND, "User with username " + username + " not found");
    }

}
