package com.dahamdi.travelmanagement.airline;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight_booking")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    @Column(name = "customer_email", nullable = false, length = 150)
    private String customerEmail;
    @Column(name = "booked_at", nullable = false)
    private LocalDateTime bookedAt;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    protected Booking() {}
    public Booking(String name, String email, BigDecimal total) {
        this.customerName = name;
        this.customerEmail = email;
        this.totalAmount = total;
        this.status = "CONFIRMED";
        this.bookedAt = LocalDateTime.now();
    }
    public Integer getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
