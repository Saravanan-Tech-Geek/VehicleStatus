package com.demo.VehicleStatusdemo.service;

import com.demo.VehicleStatusdemo.entity.Vehicle;
import com.demo.VehicleStatusdemo.exception.ResourceNotFoundException;
import com.demo.VehicleStatusdemo.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles(Optional<Boolean> availabilityStatus) {
        return availabilityStatus
                .map(vehicleRepository::findByAvailabilityStatus)
                .orElseGet(vehicleRepository::findAll);
    }

    public List<Vehicle> getAllVehiclesByAvailabilityAndFleetId(Optional<Boolean> availabilityStatus,
                                                                Optional<Integer> fleetId) {
        List<Vehicle> vehicles = vehicleRepository.findAll(); // Replace with a custom query if performance is a concern

        return vehicles.stream()
                .filter(vehicle -> availabilityStatus.map(status->vehicle.getAvailabilityStatus().equals(status)).orElse(true))
                .filter(vehicle -> fleetId.map(id -> vehicle.getFleetId().equals(id)).orElse(true))
                .collect(Collectors.toList()); // Use `toList()` instead of `collect(Collectors.toList())` for brevity (Java 16+)
    }


    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Integer id, Vehicle updatedVehicle) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setVin(updatedVehicle.getVin());
        vehicle.setVehicleName(updatedVehicle.getVehicleName());
        vehicle.setFleetId(updatedVehicle.getFleetId());
        vehicle.setDate(updatedVehicle.getDate());
        vehicle.setAvailabilityStatus(updatedVehicle.getAvailabilityStatus());

        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Integer id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }
}
