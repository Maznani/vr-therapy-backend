package com.vrappy.backend.auth;

import com.vrappy.backend.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty; // <-- NEW IMPORT
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    // This explicitly tells Jackson (the JSON library) to map the incoming JSON
    // key "fullName" to this Java field, solving the persistent NULL issue.
    @JsonProperty("fullName")
    private String fullName;
    
    private String email;
    private String password;
    private Role role;
}