package com.vrappy.backend.booking;

import com.vrappy.backend.therapist.Therapist;
import com.vrappy.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPatient(User patient);

    List<Booking> findByTherapist(Therapist therapist);
}
