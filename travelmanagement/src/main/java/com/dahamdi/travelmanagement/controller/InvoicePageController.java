package com.dahamdi.travelmanagement.controller;

import com.dahamdi.travelmanagement.airline.Booking;
import com.dahamdi.travelmanagement.airline.BookingRepository;
import com.dahamdi.travelmanagement.entity.Invoice;
import com.dahamdi.travelmanagement.service.InvoiceService;
import com.dahamdi.travelmanagement.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class InvoicePageController {

    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;

    public InvoicePageController(
            InvoiceService invoiceService,
            PaymentService paymentService,
            BookingRepository bookingRepository) {

        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.bookingRepository = bookingRepository;
    }

    // Display all invoices
    @GetMapping("/invoices")
    public String showInvoicesPage(Model model) {

        List<Invoice> invoices = invoiceService.getAllInvoices();

        Map<Integer, Double> paidAmounts = new HashMap<>();
        Map<Integer, Double> outstandingBalances = new HashMap<>();
        Map<Integer, String> calculatedStatuses = new HashMap<>();

        for (Invoice invoice : invoices) {

            Integer invoiceId = invoice.getInvoiceId();

            Double paidAmount =
                    paymentService.getCompletedPaymentTotal(invoiceId);

            Double outstandingBalance =
                    paymentService.getOutstandingBalance(invoiceId);

            String calculatedStatus =
                    paymentService.getInvoiceStatus(invoiceId);

            paidAmounts.put(invoiceId, paidAmount);
            outstandingBalances.put(invoiceId, outstandingBalance);
            calculatedStatuses.put(invoiceId, calculatedStatus);
        }

        // Get all airline bookings
        List<Booking> bookings = bookingRepository.findAll();

        model.addAttribute("invoices", invoices);
        model.addAttribute("paidAmounts", paidAmounts);
        model.addAttribute("outstandingBalances", outstandingBalances);
        model.addAttribute("calculatedStatuses", calculatedStatuses);

        // Make bookings available to invoices.html
        model.addAttribute("bookings", bookings);

        return "invoices";
    }

    // Add a new invoice
    @PostMapping("/invoices/add")
    public String addInvoice(
            @RequestParam String invoiceNumber,
            @RequestParam String customerName,
            @RequestParam String invoiceDate,
            @RequestParam Double totalAmount,
            @RequestParam String invoiceStatus,
            @RequestParam(required = false) Integer bookingId) {

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setCustomerName(customerName);
        invoice.setInvoiceDate(LocalDateTime.parse(invoiceDate));
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceStatus(invoiceStatus);

        // Connect invoice to booking if a booking was selected
        if (bookingId != null) {

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() ->
                            new RuntimeException("Booking not found"));

            invoice.setBooking(booking);
        }

        invoiceService.createInvoice(invoice);

        return "redirect:/invoices";
    }

    // Display the edit invoice page
    @GetMapping("/invoices/edit/{id}")
    public String showEditInvoicePage(
            @PathVariable Integer id,
            Model model) {

        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        List<Booking> bookings = bookingRepository.findAll();

        model.addAttribute("invoice", invoice);
        model.addAttribute("bookings", bookings);

        return "edit-invoice";
    }

    // Update an existing invoice
    @PostMapping("/invoices/update/{id}")
    public String updateInvoice(
            @PathVariable Integer id,
            @RequestParam String invoiceNumber,
            @RequestParam String customerName,
            @RequestParam String invoiceDate,
            @RequestParam Double totalAmount,
            @RequestParam String invoiceStatus,
            @RequestParam(required = false) Integer bookingId) {

        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found"));

        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setCustomerName(customerName);
        invoice.setInvoiceDate(LocalDateTime.parse(invoiceDate));
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceStatus(invoiceStatus);

        // Update booking only when a booking was selected
        if (bookingId != null) {

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() ->
                            new RuntimeException("Booking not found"));

            invoice.setBooking(booking);
        }

        invoiceService.createInvoice(invoice);

        return "redirect:/invoices";
    }

    // Delete an invoice
    @PostMapping("/invoices/delete/{id}")
    public String deleteInvoice(@PathVariable Integer id) {

        invoiceService.deleteInvoice(id);

        return "redirect:/invoices";
    }
}