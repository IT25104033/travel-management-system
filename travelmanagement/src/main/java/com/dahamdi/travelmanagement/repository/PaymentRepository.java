package com.dahamdi.travelmanagement.repository;

import com.dahamdi.travelmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.invoice.invoiceId = :invoiceId
        AND p.paymentStatus = :paymentStatus
    """)
    Double sumAmountByInvoiceIdAndPaymentStatus(
            @Param("invoiceId") Integer invoiceId,
            @Param("paymentStatus") String paymentStatus
    );

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.invoice.invoiceId = :invoiceId
        AND p.paymentStatus = 'COMPLETED'
        AND p.paymentId <> :paymentId
    """)
    Double sumOtherCompletedPayments(
            @Param("invoiceId") Integer invoiceId,
            @Param("paymentId") Integer paymentId
    );
}