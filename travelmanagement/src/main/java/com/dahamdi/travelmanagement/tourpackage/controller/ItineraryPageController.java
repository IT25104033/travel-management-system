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

    public ItineraryPageController(ItineraryRepository i,TourPackageRepository p){
        itineraryRepository=i; packageRepository=p;
    }

    @GetMapping("/itineraries/{packageId}")
    public String page(@PathVariable Integer packageId,Model model){
        TourPackage p=packageRepository.findById(packageId)
            .orElseThrow(()->new RuntimeException("Tour package not found"));
        model.addAttribute("tourPackage",p);
        model.addAttribute("itineraries",
            itineraryRepository.findByTourPackage_PackageIdOrderByDayNumberAsc(packageId));
        return "itineraries";
    }

    @PostMapping("/itineraries/add")
    public String add(@RequestParam Integer packageId,@RequestParam Integer dayNumber,
                      @RequestParam String destination,@RequestParam String activities,
                      @RequestParam String accommodation,@RequestParam String transport){
        TourPackage p=packageRepository.findById(packageId)
            .orElseThrow(()->new RuntimeException("Tour package not found"));
        Itinerary i=new Itinerary();
        i.setTourPackage(p); i.setDayNumber(dayNumber); i.setDestination(destination);
        i.setActivities(activities); i.setAccommodation(accommodation); i.setTransport(transport);
        itineraryRepository.save(i);
        return "redirect:/itineraries/"+packageId;
    }

    @PostMapping("/itineraries/delete/{id}")
    public String delete(@PathVariable Integer id,@RequestParam Integer packageId){
        itineraryRepository.deleteById(id); return "redirect:/itineraries/"+packageId;
    }
}