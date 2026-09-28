package com.dahamdi.travelmanagement.airline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BookingFlightRepository extends JpaRepository<BookingFlight, Integer> {
    List<BookingFlight> findByBooking_IdOrderById(Integer bookingId);
}
