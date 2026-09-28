package com.dahamdi.travelmanagement.airline;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AirlineService {
    private final FlightRepository flights;
    private final BookingRepository bookings;
    private final BookingFlightRepository bookingFlights;
    private final TicketRepository tickets;

    public AirlineService(FlightRepository flights, BookingRepository bookings,
                          BookingFlightRepository bookingFlights, TicketRepository tickets) {
        this.flights = flights;
        this.bookings = bookings;
        this.bookingFlights = bookingFlights;
        this.tickets = tickets;
    }

    @Transactional(readOnly = true)
    public List<AirlineController.FlightView> search(String origin, String destination, LocalDate date) {
        LocalDateTime start = date == null ? null : date.atStartOfDay();
        LocalDateTime end = date == null ? null : date.plusDays(1).atStartOfDay();
        return flights.search(blankToNull(origin), blankToNull(destination), start, end)
                .stream().map(AirlineController.FlightView::from).toList();
    }

    @Transactional
    public AirlineController.BookingView book(AirlineController.BookingRequest request) {
        // Sort IDs so concurrent checkouts acquire row locks in the same order.
        List<Integer> ids = request.flightIds().stream().distinct().sorted(Comparator.naturalOrder()).toList();
        if (ids.size() != request.flightIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Remove duplicate flights from the cart");
        }
        List<Flight> selected = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Integer id : ids) {
            Flight flight = flights.findForBooking(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight " + id + " not found"));
            if (flight.getDepartureTime().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight " + id + " has departed");
            }
            if (flight.getAvailableSeats() < 1) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight " + id + " is sold out");
            }
            selected.add(flight);
            total = total.add(flight.getPrice());
        }
        Booking booking = bookings.save(new Booking(request.customerName().trim(), request.customerEmail().trim(), total));
        for (Flight flight : selected) {
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            BookingFlight item = bookingFlights.save(new BookingFlight(booking, flight));
            tickets.save(new Ticket("TKT-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase(), item));
        }
        return detail(booking);
    }

    @Transactional(readOnly = true)
    public AirlineController.BookingView findBooking(Integer id) {
        Booking booking = bookings.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return detail(booking);
    }

    private AirlineController.BookingView detail(Booking booking) {
        List<AirlineController.TicketView> items = bookingFlights.findByBooking_IdOrderById(booking.getId())
                .stream().map(item -> {
                    Ticket ticket = tickets.findByBookingFlight_Id(item.getId()).orElseThrow();
                    Flight flight = item.getFlight();
                    return new AirlineController.TicketView(ticket.getTicketNumber(), flight.getFlightNumber(),
                            flight.getOrigin(), flight.getDestination(), flight.getDepartureTime(),
                            item.getPriceAtBooking());
                }).toList();
        return new AirlineController.BookingView(booking.getId(), booking.getCustomerName(),
                booking.getCustomerEmail(), booking.getBookedAt(), booking.getStatus(),
                booking.getTotalAmount(), items);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
