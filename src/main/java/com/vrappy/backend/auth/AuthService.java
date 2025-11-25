package com.vrappy.backend.auth;

import com.vrappy.backend.config.JwtService;
import com.vrappy.backend.therapist.Therapist;
import com.vrappy.backend.therapist.TherapistRepository;
import com.vrappy.backend.user.Role;
import com.vrappy.backend.user.User;
import com.vrappy.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TherapistRepository therapistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest request) {

        User user = User.builder()
                .name(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = repository.save(user);

        // 3. Auto-create Therapist Profile if Role is THERAPIST
        if (request.getRole() == Role.THERAPIST) {
            
            Therapist therapist = Therapist.builder()
                    // --- THE FIX IS HERE: (long) ---
                    .id((long) savedUser.getId()) 
                    // -------------------------------
                    .fullName(request.getFullName()) 
                    .specialization("General Therapy")
                    .experienceYears(0)
                    .rating(5.0) 
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

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}