package com.dahamdi.travelmanagement.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    @Query("select f from Flight f where (:origin is null or lower(f.origin) like lower(concat('%', :origin, '%'))) and (:destination is null or lower(f.destination) like lower(concat('%', :destination, '%'))) and (:start is null or f.departureTime >= :start) and (:end is null or f.departureTime < :end) order by f.departureTime")
    List<Flight> search(@Param("origin") String origin, @Param("destination") String destination,
                        @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Flight f where f.id = :id")
    Optional<Flight> findForBooking(@Param("id") Integer id);
}
