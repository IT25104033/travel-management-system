package com.dahamdi.travelmanagement.controller;

import com.dahamdi.travelmanagement.entity.Invoice;
import com.dahamdi.travelmanagement.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class InvoicePageController {

    private final InvoiceService invoiceService;

    public InvoicePageController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @GetMapping("/invoices")
    public String showInvoicesPage(Model model) {

        List<Invoice> invoices = invoiceService.getAllInvoices();

        model.addAttribute("invoices", invoices);

        return "invoices";
    }

    // Add
    @PostMapping("/invoices/add")
    public String addInvoice(
            @RequestParam String invoiceNumber,
            @RequestParam String customerName,
            @RequestParam String invoiceDate,
            @RequestParam Double totalAmount,
            @RequestParam String invoiceStatus) {

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setCustomerName(customerName);
        invoice.setInvoiceDate(LocalDateTime.parse(invoiceDate));
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceStatus(invoiceStatus);

        invoiceService.createInvoice(invoice);

        return "redirect:/invoices";
    }

    // Display edit invoice page
    @GetMapping("/invoices/edit/{id}")
    public String showEditInvoicePage(
            @PathVariable Integer id,
            Model model) {

        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        model.addAttribute("invoice", invoice);

        return "edit-invoice";
    }

    // Update
    @PostMapping("/invoices/update/{id}")
    public String updateInvoice(
            @PathVariable Integer id,
            @RequestParam String invoiceNumber,
            @RequestParam String customerName,
            @RequestParam String invoiceDate,
            @RequestParam Double totalAmount,
            @RequestParam String invoiceStatus) {

        Invoice invoice = invoiceService.getInvoiceById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setCustomerName(customerName);
        invoice.setInvoiceDate(LocalDateTime.parse(invoiceDate));
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceStatus(invoiceStatus);

        invoiceService.createInvoice(invoice);

        return "redirect:/invoices";
    }

    // Delete
    @PostMapping("/invoices/delete/{id}")
    public String deleteInvoice(@PathVariable Integer id) {

        invoiceService.deleteInvoice(id);

        return "redirect:/invoices";
    }
}