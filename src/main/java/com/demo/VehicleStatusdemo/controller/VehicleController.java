package com.demo.VehicleStatusdemo.controller;

import com.demo.VehicleStatusdemo.entity.Vehicle;
import com.demo.VehicleStatusdemo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    // Constructor injection (preferred)
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    // Get all vehicles with optional availability filter
    @GetMapping
    public List<Vehicle> getVehicles(@RequestParam Optional<Boolean> availabilityStatus) {
        return vehicleService.getAllVehicles(availabilityStatus);
    }

    // Filter by availability and fleetId
    @GetMapping("/filter")
    public List<Vehicle> getVehiclesByAvailabilityAndFleetId(
            @RequestParam Optional<Boolean> availabilityStatus,
            @RequestParam Optional<Integer> fleetId) {
        return vehicleService.getAllVehiclesByAvailabilityAndFleetId(availabilityStatus, fleetId);
    }

    // Create a new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    // Update a vehicle by ID
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Integer id,
            @Valid @RequestBody Vehicle updatedVehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, updatedVehicle));
    }

    // Delete a vehicle by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
