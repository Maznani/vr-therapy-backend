package com.vrappy.backend.auth;

import com.vrappy.backend.user.User;
import com.vrappy.backend.user.Role; // Ensure you have this import
import com.vrappy.backend.user.UserRepository;
import com.vrappy.backend.therapist.Therapist; // Import your Therapist entity
import com.vrappy.backend.therapist.TherapistRepository; // Import your Therapist Repository
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vrappy.backend.config.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TherapistRepository therapistRepository; // <--- 1. INJECT THIS
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest request) {

        var user = User.builder()
                .name(request.getFullName()) 
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // 2. CAPTURE THE SAVED USER (We need the generated ID)
        var savedUser = repository.save(user);

        // 3. THE FIX: Auto-create Therapist Profile if Role matches
        if (request.getRole() == Role.THERAPIST) {
            var therapist = Therapist.builder()
                    .id(savedUser.getId()) // Link to the new User ID
                    .fullName(request.getFullName()) // Copy the name
                    .specialization("General Therapy") // Default values...
                    .experienceYears(0)
                    .rating(5.0) // Give them a perfect start!
                    .about("New therapist profile.")
                    .gender("Prefer not to say")
                    .build();

            therapistRepository.save(therapist);
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}