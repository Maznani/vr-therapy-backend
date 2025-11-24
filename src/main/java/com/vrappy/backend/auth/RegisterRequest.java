package com.vrappy.backend.auth;

import com.vrappy.backend.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String fullName;  // <-- FIXED
    private String email;
    private String password;
    private Role role;
}
