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

        // 1. Use fullName directly from the request
        // This prevents the "Unknown Therapist" issue on the frontend
        String combinedName = request.getFullName();

        User user = User.builder()
                .name(combinedName) // Use the combined name for the User table
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // 2. Save User (Generates the ID)
        User savedUser = repository.save(user);

        // 3. Auto-create Therapist Profile if Role is THERAPIST
        if (request.getRole() == Role.THERAPIST) {
            
            Therapist therapist = Therapist.builder()
                    // FIX: Cast Integer ID from User to Long for Therapist
                    .id((long) savedUser.getId()) 
                    
                    // FIX: Use the combined name here too so the profile has a name
                    .fullName(combinedName) 
                    
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