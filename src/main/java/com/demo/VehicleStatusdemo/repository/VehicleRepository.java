package com.demo.VehicleStatusdemo.repository;

import com.demo.VehicleStatusdemo.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    // Find vehicles by availability status
    List<Vehicle> findByAvailabilityStatus(Boolean availabilityStatus);
}
