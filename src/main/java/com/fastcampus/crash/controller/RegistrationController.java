package com.fastcampus.crash.controller;

import com.fastcampus.crash.model.crashsession.CrashSession;
import com.fastcampus.crash.model.entity.UserEntity;
import com.fastcampus.crash.model.registration.Registration;
import com.fastcampus.crash.model.registration.RegistrationPostRequestBody;
import com.fastcampus.crash.model.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    // 별도로 외부에 오픈해 놓은게 아니기 때문에 JWT토큰을 들고 요청하는 api이기 때문에 authentication에 접근해서 저장되어있는 유저 정보를 가져올수 있다.
    @GetMapping()//controller단에 있는 함수에 Authentication 추가해 주는것으로 그 사용자 인증 정보에 접근할수 있다.
    public ResponseEntity<List<Registration>> getRegistrations(Authentication authentication) {
        List<Registration> registrations = registrationService.getRegistrationsByCurrentUser((UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<Registration> getRegistrationByRegistrationId(@PathVariable Long registrationId, Authentication authentication) {
        Registration registration = registrationService.getRegistrationByRegistrationIdByCurrentUser(registrationId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registration);
    }

    @PostMapping()
    public ResponseEntity<Registration> createRegistration(@Valid @RequestBody RegistrationPostRequestBody registrationPostRequestBody, Authentication authentication) {
        Registration registration = registrationService.createRegistrationByCurrentUser(registrationPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registration);
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long registrationId, Authentication authentication) {
        registrationService.deleteRegistrationByRegistrationIdAndByCurrentUser(registrationId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
