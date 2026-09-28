package com.dahamdi.travelmanagement.service;

import com.dahamdi.travelmanagement.entity.Invoice;
import com.dahamdi.travelmanagement.entity.Payment;
import com.dahamdi.travelmanagement.repository.InvoiceRepository;
import com.dahamdi.travelmanagement.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository) {

        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }

    public Double getCompletedPaymentTotal(Integer invoiceId) {

        Double total = paymentRepository
                .sumAmountByInvoiceIdAndPaymentStatus(
                        invoiceId,
                        "COMPLETED"
                );

        return total != null ? total : 0.0;
    }
    public Double getOutstandingBalanceExcludingPayment(
            Integer invoiceId,
            Integer paymentId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        Double otherCompletedPayments =
                paymentRepository.sumOtherCompletedPayments(
                        invoiceId,
                        paymentId
                );

        return invoice.getTotalAmount() - otherCompletedPayments;
    }

    public Double getOutstandingBalance(Integer invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        Double completedPayments =
                getCompletedPaymentTotal(invoiceId);

        return invoice.getTotalAmount() - completedPayments;
    }

    public String getInvoiceStatus(Integer invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        Double totalAmount = invoice.getTotalAmount();

        Double completedPayments =
                getCompletedPaymentTotal(invoiceId);

        if (completedPayments == 0) {
            return "UNPAID";
        }

        if (completedPayments < totalAmount) {
            return "PARTIAL";
        }

        return "PAID";
    }
}