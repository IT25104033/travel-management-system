package com.dahamdi.travelmanagement.airline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Optional<Ticket> findByBookingFlight_Id(Integer bookingFlightId);
}
