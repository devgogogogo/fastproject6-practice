package com.fastcampus.crash.controller;

import com.fastcampus.crash.model.service.SessionSpeakerService;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeaker;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session-speakers")
@RequiredArgsConstructor
public class SessionSpeakerController {

    private final SessionSpeakerService sessionSpeakerService;

    @GetMapping()
    public ResponseEntity<List<SessionSpeaker>> getSessionSpeakers() {
        List<SessionSpeaker> sessionSpeakers = sessionSpeakerService.getSessionSpeakers();
        return ResponseEntity.ok(sessionSpeakers);
    }

    @GetMapping("/{speakersId}")
    public ResponseEntity<SessionSpeaker> getSessionSpeakerBySpeakerId(@PathVariable Long speakersId) {
        SessionSpeaker sessionSpeaker = sessionSpeakerService.getSessionSpeakerBySpeakerId(speakersId);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @PostMapping
    public ResponseEntity<SessionSpeaker> createdSessionSpeaker(@Valid @RequestBody SessionSpeakerPostRequestBody sessionSpeakerPostRequestBody) {
        SessionSpeaker sessionSpeaker = sessionSpeakerService.createdSessionSpeaker(sessionSpeakerPostRequestBody);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @PatchMapping("/{speakersId}")
    public ResponseEntity<SessionSpeaker> updateSesionSpeaker(
            @PathVariable Long speakersId,
            @RequestBody SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody) {
        SessionSpeaker sessionSpeaker = sessionSpeakerService.updateSessionSpeaker(speakersId,sessionSpeakerPatchRequestBody);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @DeleteMapping("/{speakersId}")
    public ResponseEntity<Void> deleteSessionSpeaker(@PathVariable Long speakersId) {
        sessionSpeakerService.deleteSessionSpeaker(speakersId);
        return ResponseEntity.noContent().build();
    }
}
