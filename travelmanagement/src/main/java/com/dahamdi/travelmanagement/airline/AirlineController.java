package com.dahamdi.travelmanagement.airline;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/airline")
public class AirlineController {
    private final AirlineService service;
    public AirlineController(AirlineService service) { this.service = service; }

    @GetMapping("/flights")
    public List<FlightView> search(@RequestParam(required = false) String origin,
                                   @RequestParam(required = false) String destination,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.search(origin, destination, date);
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingView book(@Valid @RequestBody BookingRequest request) { return service.book(request); }

    @GetMapping("/bookings/{id}")
    public BookingView findBooking(@PathVariable Integer id) { return service.findBooking(id); }

    public record BookingRequest(@NotBlank String customerName, @NotBlank @Email String customerEmail,
                                 @NotEmpty List<@NotNull Integer> flightIds) {}
    public record FlightView(Integer id, String flightNumber, String airlineName, String origin,
                             String destination, LocalDateTime departureTime, LocalDateTime arrivalTime,
                             BigDecimal price, Integer availableSeats) {
        public static FlightView from(Flight flight) {
            return new FlightView(flight.getId(), flight.getFlightNumber(), flight.getAirlineName(),
                    flight.getOrigin(), flight.getDestination(), flight.getDepartureTime(),
                    flight.getArrivalTime(), flight.getPrice(), flight.getAvailableSeats());
        }
    }
    public record TicketView(String ticketNumber, String flightNumber, String origin,
                             String destination, LocalDateTime departureTime, BigDecimal price) {}
    public record BookingView(Integer bookingId, String customerName, String customerEmail,
                              LocalDateTime bookedAt, String status, BigDecimal totalAmount,
                              List<TicketView> tickets) {}
}
