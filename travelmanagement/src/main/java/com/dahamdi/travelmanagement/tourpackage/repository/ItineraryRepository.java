package com.dahamdi.travelmanagement.tourpackage.repository;
import com.dahamdi.travelmanagement.tourpackage.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ItineraryRepository extends JpaRepository<Itinerary,Integer>{
    List<Itinerary> findByTourPackage_PackageIdOrderByDayNumberAsc(Integer packageId);
}