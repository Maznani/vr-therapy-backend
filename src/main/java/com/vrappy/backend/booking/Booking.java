package com.vrappy.backend.booking;

import com.vrappy.backend.therapist.Therapist;
import com.vrappy.backend.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    @ManyToOne
    private Therapist therapist;

    private String sessionDate;
    private String sessionTime;
    private int sessionDuration;
    private String sessionGoal;
    private String scenarioUsed;
}
