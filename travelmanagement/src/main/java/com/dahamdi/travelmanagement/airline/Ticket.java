package com.dahamdi.travelmanagement.airline;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight_ticket")
public class Ticket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer id;
    @Column(name = "ticket_number", nullable = false, unique = true, length = 40)
    private String ticketNumber;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_flight_id", nullable = false, unique = true)
    private BookingFlight bookingFlight;
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    protected Ticket() {}
    public Ticket(String number, BookingFlight bookingFlight) {
        this.ticketNumber = number;
        this.bookingFlight = bookingFlight;
        this.issuedAt = LocalDateTime.now();
    }
    public String getTicketNumber() { return ticketNumber; }
    public BookingFlight getBookingFlight() { return bookingFlight; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
}
