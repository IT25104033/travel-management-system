package com.dahamdi.travelmanagement.airline;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "booking_flight", uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "flight_id"}))
public class BookingFlight {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_flight_id")
    private Integer id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    @Column(name = "price_at_booking", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtBooking;

    protected BookingFlight() {}
    public BookingFlight(Booking booking, Flight flight) {
        this.booking = booking;
        this.flight = flight;
        this.priceAtBooking = flight.getPrice();
    }
    public Integer getId() { return id; }
    public Booking getBooking() { return booking; }
    public Flight getFlight() { return flight; }
    public BigDecimal getPriceAtBooking() { return priceAtBooking; }
}
