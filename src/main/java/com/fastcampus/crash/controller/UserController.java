package com.fastcampus.crash.controller;

import com.fastcampus.crash.model.service.UserService;
import com.fastcampus.crash.model.user.User;
import com.fastcampus.crash.model.user.UserAuthenticationResponse;
import com.fastcampus.crash.model.user.UserLoginRequestBody;
import com.fastcampus.crash.model.user.UserSignUpRequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<User> signUp(@Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        User user = userService.signUp(userSignUpRequestBody);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> login(@Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {

        UserAuthenticationResponse response = userService.login(userLoginRequestBody);

        return ResponseEntity.ok(response);
    }
}
