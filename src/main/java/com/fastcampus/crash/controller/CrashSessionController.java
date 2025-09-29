package com.fastcampus.crash.controller;

import com.fastcampus.crash.model.crashsession.CrashSession;
import com.fastcampus.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.fastcampus.crash.model.crashsession.CrashSessionPostRequestBody;
import com.fastcampus.crash.model.crashsession.CrashSessionRegistrationStatus;
import com.fastcampus.crash.model.entity.UserEntity;
import com.fastcampus.crash.model.service.CrashSessionService;
import com.fastcampus.crash.model.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crash-sessions")
@RequiredArgsConstructor
public class CrashSessionController {

    private final CrashSessionService crashSessionService;
    private final RegistrationService registrationService;

    @GetMapping()
    public ResponseEntity<List<CrashSession>> getCrashSessions() {
        List<CrashSession> crashSessions = crashSessionService.getCrashSessions();
        return ResponseEntity.ok(crashSessions);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CrashSession> getCrashSessionBySessionId(@PathVariable Long sessionId) {
        CrashSession crashSession = crashSessionService.getCrashSessionBySessionId(sessionId);
        return ResponseEntity.ok(crashSession);
    }

    @GetMapping("/{sessionId}/registration-status")
    public ResponseEntity<CrashSessionRegistrationStatus> getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(
            @PathVariable Long sessionId,
//            @AuthenticationPrincipal UserEntity userEntity
            Authentication authentication // -->@AuthenticationPrincipal UserEntity userEntity 같은 표현입 이렇게 사용해도된다.
    ) {

        CrashSessionRegistrationStatus crashSessionRegistrationStatus = registrationService.getCrashSessionRegistrationstatusBySessionId(sessionId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(crashSessionRegistrationStatus);
    }

    @PostMapping
    public ResponseEntity<CrashSession> createCrashSession(@Valid @RequestBody CrashSessionPostRequestBody crashSessionPostRequestBody) {
        CrashSession crashSession = crashSessionService.createCrashSession(crashSessionPostRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<CrashSession> updateCrashSession(@PathVariable Long sessionId, @RequestBody CrashSessionPatchRequestBody crashSessionPatchRequestBody) {
        CrashSession crashSession = crashSessionService.updateCrashSession(sessionId, crashSessionPatchRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteCrashSession(@PathVariable Long sessionId) {
        crashSessionService.deleteCrashSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
