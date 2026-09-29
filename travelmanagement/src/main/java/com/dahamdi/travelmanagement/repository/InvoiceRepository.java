package com.dahamdi.travelmanagement.repository;

import com.dahamdi.travelmanagement.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findByBooking_Id(Integer bookingId);
}