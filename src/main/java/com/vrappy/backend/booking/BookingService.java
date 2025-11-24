package com.vrappy.backend.booking;

import com.vrappy.backend.therapist.Therapist;
import com.vrappy.backend.therapist.TherapistRepository;
import com.vrappy.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TherapistRepository therapistRepository;

    public Booking createBooking(BookingRequest req, User patient) {

        Therapist therapist = therapistRepository.findById(req.getTherapistId())
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        Booking booking = Booking.builder()
                .patient(patient)
                .therapist(therapist)
                .sessionDate(req.getSessionDate())
                .sessionTime(req.getSessionTime())
                .sessionDuration(req.getSessionDuration())
                .sessionGoal(req.getSessionGoal())
                .scenarioUsed(req.getScenarioUsed())
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForUser(User user) {
        return bookingRepository.findByPatient(user);
    }

    public List<Booking> getBookingsForTherapist(Long therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));
        return bookingRepository.findByTherapist(therapist);
    }

    // 🔥 NEW METHOD
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
