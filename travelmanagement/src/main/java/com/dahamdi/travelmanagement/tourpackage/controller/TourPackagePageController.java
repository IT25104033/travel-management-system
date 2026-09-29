package com.dahamdi.travelmanagement.tourpackage.controller;

import com.dahamdi.travelmanagement.tourpackage.entity.TourPackage;
import com.dahamdi.travelmanagement.tourpackage.repository.TourPackageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class TourPackagePageController {

    private final TourPackageRepository repository;

    public TourPackagePageController(TourPackageRepository repository) {
        this.repository = repository;
    }

    // =========================
    // VIEW ALL PACKAGES
    // =========================
    @GetMapping("/packages")
    public String packages(Model model) {

        model.addAttribute("packages", repository.findAll());

        return "packages";
    }

    // =========================
    // ADD PACKAGE
    // =========================
    @PostMapping("/packages/add")
    public String add(
            @RequestParam String packageName,
            @RequestParam String description,
            @RequestParam Integer duration,
            @RequestParam BigDecimal price,
            @RequestParam String status) {

        TourPackage tourPackage = new TourPackage();

        tourPackage.setPackageName(packageName);
        tourPackage.setDescription(description);
        tourPackage.setDuration(duration);
        tourPackage.setPrice(price);
        tourPackage.setStatus(status);

        repository.save(tourPackage);

        return "redirect:/packages";
    }

    // =========================
    // SHOW EDIT PAGE
    // =========================
    @GetMapping("/packages/edit/{id}")
    public String showEditPage(
            @PathVariable Integer id,
            Model model) {

        TourPackage tourPackage = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tour package not found"));

        model.addAttribute("tourPackage", tourPackage);

        return "edit-package";
    }

    // =========================
    // UPDATE PACKAGE
    // =========================
    @PostMapping("/packages/update/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestParam String packageName,
            @RequestParam String description,
            @RequestParam Integer duration,
            @RequestParam BigDecimal price,
            @RequestParam String status) {

        TourPackage tourPackage = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tour package not found"));

        tourPackage.setPackageName(packageName);
        tourPackage.setDescription(description);
        tourPackage.setDuration(duration);
        tourPackage.setPrice(price);
        tourPackage.setStatus(status);

        repository.save(tourPackage);

        return "redirect:/packages";
    }

    // =========================
    // DELETE PACKAGE
    // =========================
    @PostMapping("/packages/delete/{id}")
    public String delete(
            @PathVariable Integer id) {

        repository.deleteById(id);

        return "redirect:/packages";
    }
}