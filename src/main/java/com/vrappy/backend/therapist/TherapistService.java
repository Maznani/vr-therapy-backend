package com.vrappy.backend.therapist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TherapistService {

    private final TherapistRepository therapistRepository;

    public List<Therapist> getAllTherapists() {
        return therapistRepository.findAll();
    }

    public Therapist getTherapistById(Long id) {
        return therapistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));
    }
}


