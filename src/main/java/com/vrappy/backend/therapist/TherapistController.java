package com.vrappy.backend.therapist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/therapists")
@RequiredArgsConstructor
public class TherapistController {

    private final TherapistRepository therapistRepository;

    @GetMapping
    public ResponseEntity<List<Therapist>> getAllTherapists() {
        // This returns the exact JSON structure you tested with curl
        return ResponseEntity.ok(therapistRepository.findAll());
    }
}