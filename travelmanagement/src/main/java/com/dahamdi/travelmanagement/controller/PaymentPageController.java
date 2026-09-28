package com.dahamdi.travelmanagement.controller;

import com.dahamdi.travelmanagement.entity.Invoice;
import com.dahamdi.travelmanagement.entity.Payment;
import com.dahamdi.travelmanagement.service.InvoiceService;
import com.dahamdi.travelmanagement.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PaymentPageController {

    private final PaymentService paymentService;
    private final InvoiceService invoiceService;

    public PaymentPageController(
            PaymentService paymentService,
            InvoiceService invoiceService) {

        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
    }

    // Display all payments
    @GetMapping("/payments")
    public String showPaymentsPage(Model model) {

        List<Payment> payments = paymentService.getAllPayments();
        List<Invoice> invoices = invoiceService.getAllInvoices();

        model.addAttribute("payments", payments);
        model.addAttribute("invoices", invoices);

        return "payment";
    }

    // Add a new payment
    @PostMapping("/payments/add")
    public String addPayment(
            @RequestParam Integer invoiceId,
            @RequestParam String paymentDate,
            @RequestParam Double amount,
            @RequestParam String paymentMethod,
            @RequestParam String paymentStatus,
            Model model) {

        Invoice invoice = invoiceService.getInvoiceById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        Double outstandingBalance =
                paymentService.getOutstandingBalance(invoiceId);

        if (amount > outstandingBalance) {

            List<Payment> payments = paymentService.getAllPayments();
            List<Invoice> invoices = invoiceService.getAllInvoices();

            model.addAttribute("payments", payments);
            model.addAttribute("invoices", invoices);

            model.addAttribute(
                    "errorMessage",
                    "Payment amount cannot be greater than the outstanding balance."
            );

            return "payment";
        }

        Payment payment = new Payment();

        payment.setInvoice(invoice);
        payment.setPaymentDate(LocalDateTime.parse(paymentDate));
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);

        paymentService.createPayment(payment);

        return "redirect:/payments";
    }

    // Display the edit payment page
    @GetMapping("/payments/edit/{id}")
    public String showEditPaymentPage(
            @PathVariable Integer id,
            Model model) {

        Payment payment = paymentService.getPaymentById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        List<Invoice> invoices = invoiceService.getAllInvoices();

        model.addAttribute("payment", payment);
        model.addAttribute("invoices", invoices);

        return "edit-payment";
    }

    // Update an existing payment
    @PostMapping("/payments/update/{id}")
    public String updatePayment(
            @PathVariable Integer id,
            @RequestParam Integer invoiceId,
            @RequestParam String paymentDate,
            @RequestParam Double amount,
            @RequestParam String paymentMethod,
            @RequestParam String paymentStatus,
            Model model) {

        Payment payment = paymentService.getPaymentById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        Invoice invoice = invoiceService.getInvoiceById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        Double outstandingBalance =
                paymentService.getOutstandingBalanceExcludingPayment(
                        invoiceId,
                        id
                );

        if (amount > outstandingBalance) {

            List<Invoice> invoices = invoiceService.getAllInvoices();

            model.addAttribute("payment", payment);
            model.addAttribute("invoices", invoices);

            model.addAttribute(
                    "errorMessage",
                    "Payment amount cannot be greater than the outstanding balance."
            );

            return "edit-payment";
        }

        payment.setInvoice(invoice);
        payment.setPaymentDate(LocalDateTime.parse(paymentDate));
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);

        paymentService.createPayment(payment);

        return "redirect:/payments";
    }

    // Delete a payment
    @PostMapping("/payments/delete/{id}")
    public String deletePayment(@PathVariable Integer id) {

        paymentService.deletePayment(id);

        return "redirect:/payments";
    }
}