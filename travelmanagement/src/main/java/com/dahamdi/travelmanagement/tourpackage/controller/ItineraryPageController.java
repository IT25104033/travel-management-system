
package com.dahamdi.travelmanagement.tourpackage.controller;

import com.dahamdi.travelmanagement.tourpackage.entity.Itinerary;
import com.dahamdi.travelmanagement.tourpackage.entity.TourPackage;
import com.dahamdi.travelmanagement.tourpackage.repository.ItineraryRepository;
import com.dahamdi.travelmanagement.tourpackage.repository.TourPackageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItineraryPageController {

    private final ItineraryRepository itineraryRepository;
    private final TourPackageRepository packageRepository;

    public ItineraryPageController(
            ItineraryRepository itineraryRepository,
            TourPackageRepository packageRepository) {
        this.itineraryRepository = itineraryRepository;
        this.packageRepository = packageRepository;
    }

    @GetMapping("/itineraries/{packageId}")
    public String page(
            @PathVariable Integer packageId,
            Model model) {

        loadItineraryPage(packageId, model);
        return "itineraries";
    }

    @PostMapping("/itineraries/add")
    public String add(
            @RequestParam Integer packageId,
            @RequestParam Integer dayNumber,
            @RequestParam String destination,
            @RequestParam String activities,
            @RequestParam(required = false) String accommodation,
            @RequestParam(required = false) String transport,
            Model model) {

        TourPackage tourPackage = packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new RuntimeException("Tour package not found"));

        if (dayNumber < 1 || dayNumber > tourPackage.getDuration()) {
            model.addAttribute(
                    "errorMessage",
                    "Day number must be between 1 and "
                            + tourPackage.getDuration() + ".");
            loadItineraryPage(packageId, model);
            return "itineraries";
        }

        if (itineraryRepository
                .existsByTourPackage_PackageIdAndDayNumber(
                        packageId, dayNumber)) {

            model.addAttribute(
                    "errorMessage",
                    "Day " + dayNumber
                            + " already exists for this package.");

            loadItineraryPage(packageId, model);
            return "itineraries";
        }

        Itinerary itinerary = new Itinerary();
        itinerary.setTourPackage(tourPackage);
        itinerary.setDayNumber(dayNumber);
        itinerary.setDestination(destination);
        itinerary.setActivities(activities);
        itinerary.setAccommodation(accommodation);
        itinerary.setTransport(transport);

        itineraryRepository.save(itinerary);

        return "redirect:/itineraries/" + packageId;
    }

    @GetMapping("/itineraries/edit/{id}")
    public String showEditPage(
            @PathVariable Integer id,
            Model model) {

        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Itinerary not found"));

        model.addAttribute("itinerary", itinerary);
        model.addAttribute("tourPackage", itinerary.getTourPackage());

        return "edit-itinerary";
    }

    @PostMapping("/itineraries/update/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestParam Integer dayNumber,
            @RequestParam String destination,
            @RequestParam String activities,
            @RequestParam(required = false) String accommodation,
            @RequestParam(required = false) String transport,
            Model model) {

        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Itinerary not found"));

        Integer packageId =
                itinerary.getTourPackage().getPackageId();

        TourPackage tourPackage = itinerary.getTourPackage();

        if (dayNumber < 1 || dayNumber > tourPackage.getDuration()) {
            model.addAttribute(
                    "errorMessage",
                    "Day number must be between 1 and "
                            + tourPackage.getDuration() + ".");
            model.addAttribute("itinerary", itinerary);
            model.addAttribute("tourPackage", tourPackage);
            return "edit-itinerary";
        }

        boolean duplicate =
                itineraryRepository
                        .existsByTourPackage_PackageIdAndDayNumber(
                                packageId, dayNumber);

        if (duplicate && !dayNumber.equals(itinerary.getDayNumber())) {
            model.addAttribute(
                    "errorMessage",
                    "Day " + dayNumber
                            + " already exists for this package.");

            model.addAttribute("itinerary", itinerary);
            model.addAttribute("tourPackage", tourPackage);
            return "edit-itinerary";
        }

        itinerary.setDayNumber(dayNumber);
        itinerary.setDestination(destination);
        itinerary.setActivities(activities);
        itinerary.setAccommodation(accommodation);
        itinerary.setTransport(transport);

        itineraryRepository.save(itinerary);

        return "redirect:/itineraries/" + packageId;
    }

    @PostMapping("/itineraries/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            @RequestParam Integer packageId) {

        itineraryRepository.deleteById(id);

        return "redirect:/itineraries/" + packageId;
    }

    private void loadItineraryPage(
            Integer packageId,
            Model model) {

        TourPackage tourPackage = packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new RuntimeException("Tour package not found"));

        model.addAttribute("tourPackage", tourPackage);

        model.addAttribute(
                "itineraries",
                itineraryRepository
                        .findByTourPackage_PackageIdOrderByDayNumberAsc(
                                packageId));
    }
}
