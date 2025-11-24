package com.vrappy.backend;

import com.vrappy.backend.therapist.Therapist;
import com.vrappy.backend.therapist.TherapistRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    @Bean
    CommandLineRunner seedTherapists(TherapistRepository therapistRepository) {
        return args -> {
            if (therapistRepository.count() == 0) {
                therapistRepository.saveAll(List.of(
                        Therapist.builder()
                                .fullName("Dr. Sarah Mitchell")
                                .gender("Female")
                                .specialization("Anxiety & Panic Disorders")
                                .experienceYears(7)
                                .about("Specializes in CBT for anxiety, panic, and stress-related issues.")
                                .rating(4.8)
                                .build(),

                        Therapist.builder()
                                .fullName("Dr. Omar Khan")
                                .gender("Male")
                                .specialization("PTSD & Trauma")
                                .experienceYears(10)
                                .about("Works with trauma, PTSD, and exposure therapy in VR environments.")
                                .rating(4.9)
                                .build(),

                        Therapist.builder()
                                .fullName("Dr. Emily Chen")
                                .gender("Female")
                                .specialization("Depression & Burnout")
                                .experienceYears(5)
                                .about("Helps patients manage depression, burnout, and low mood.")
                                .rating(4.7)
                                .build()
                ));
            }
        };
    }}
