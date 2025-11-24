package com.vrappy.backend.booking;

import com.vrappy.backend.user.User;
import com.vrappy.backend.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        System.out.println("📥 NEW BOOKING REQUEST RECEIVED");
        System.out.println("➡️ RAW REQUEST: " + request);
        System.out.println("➡️ USER PRINCIPAL: " + principal);

        if (principal == null) {
            System.out.println("❌ ERROR: principal is NULL (token missing or invalid)");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User patient = principal.getUser();

        System.out.println("➡️ PATIENT OBJECT: " + patient);

        try {
            Booking newBooking = bookingService.createBooking(request, patient);
            System.out.println("✅ BOOKING CREATED: " + newBooking.getId());
            return ResponseEntity.ok(newBooking);
        } catch (Exception e) {
            System.out.println("❌ BOOKING ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        System.out.println("📥 Fetching all bookings");
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/my-sessions")
    public ResponseEntity<List<Booking>> getMySessions(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        System.out.println("📥 Fetching sessions for user: " + principal);
        return ResponseEntity.ok(
                bookingService.getBookingsForUser(principal.getUser())
        );
    }

    @GetMapping("/therapist-sessions/{therapistId}")
    public ResponseEntity<List<Booking>> getTherapistSessions(
            @PathVariable Long therapistId
    ) {
        System.out.println("📥 Fetching sessions for therapist ID: " + therapistId);
        return ResponseEntity.ok(
                bookingService.getBookingsForTherapist(therapistId)
        );
    }
}
