package com.dahamdi.travelmanagement.airline;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
public class Flight {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Integer id;
    @Column(name = "flight_number", nullable = false, unique = true, length = 20)
    private String flightNumber;
    @Column(name = "airline_name", nullable = false, length = 100)
    private String airlineName;
    @Column(nullable = false, length = 100)
    private String origin;
    @Column(nullable = false, length = 100)
    private String destination;
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    protected Flight() {}
    public Integer getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getAirlineName() { return airlineName; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public BigDecimal getPrice() { return price; }
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer seats) { this.availableSeats = seats; }
}
