package com.fastcampus.crash.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserSignUpRequestBody(
        @NotEmpty
        String username,
        @NotEmpty(message = "비밀번호는 필수 입니다.")
        String password,
        @NotEmpty
        String name,
        @NotEmpty @Email
        String email
) {

}
