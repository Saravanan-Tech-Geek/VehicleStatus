package com.demo.VehicleStatusdemo.service;

import com.demo.VehicleStatusdemo.entity.Vehicle;
import com.demo.VehicleStatusdemo.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);

        vehicle1 = new Vehicle();
        vehicle1.setId(1);
        vehicle1.setVin("VIN123");
        vehicle1.setVehicleName("Car A");
        vehicle1.setFleetId(100);
        // Convert LocalDate to java.util.Date
        vehicle1.setDate(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        vehicle1.setAvailabilityStatus(true);

        vehicle2 = new Vehicle();
        vehicle2.setId(2);
        vehicle2.setVin("VIN456");
        vehicle2.setVehicleName("Car B");
        vehicle2.setFleetId(200);
        vehicle2.setDate(Date.from(LocalDate.of(2023, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        vehicle2.setAvailabilityStatus(false);
    }

    @Test
    void testGetAllVehicles_withAvailability() {
        when(vehicleRepository.findByAvailabilityStatus(true)).thenReturn(List.of(vehicle1));

        List<Vehicle> result = vehicleService.getAllVehicles(Optional.of(true));

        assertEquals(1, result.size());
        assertTrue(result.get(0).getAvailabilityStatus());
        verify(vehicleRepository).findByAvailabilityStatus(true);
    }

    @Test
    void testGetAllVehicles_withoutAvailability() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<Vehicle> result = vehicleService.getAllVehicles(Optional.empty());

        assertEquals(2, result.size());
        verify(vehicleRepository).findAll();
    }

    @Test
    void testGetAllVehiclesByAvailabilityAndFleetId_bothFilters() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<Vehicle> result = vehicleService.getAllVehiclesByAvailabilityAndFleetId(Optional.of(true), Optional.of(100));

        assertEquals(1, result.size());
        assertEquals(vehicle1.getId(), result.get(0).getId());
    }

    @Test
    void testGetAllVehiclesByAvailabilityAndFleetId_availabilityOnly() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<Vehicle> result = vehicleService.getAllVehiclesByAvailabilityAndFleetId(Optional.of(false), Optional.empty());

        assertEquals(1, result.size());
        assertFalse(result.get(0).getAvailabilityStatus());
    }

    @Test
    void testGetAllVehiclesByAvailabilityAndFleetId_fleetIdOnly() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<Vehicle> result = vehicleService.getAllVehiclesByAvailabilityAndFleetId(Optional.empty(), Optional.of(200));

        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getFleetId());
    }

    @Test
    void testGetAllVehiclesByAvailabilityAndFleetId_noFilters() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<Vehicle> result = vehicleService.getAllVehiclesByAvailabilityAndFleetId(Optional.empty(), Optional.empty());

        assertEquals(2, result.size());
    }

    @Test
    void testCreateVehicle() {
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        Vehicle created = vehicleService.createVehicle(vehicle1);

        assertEquals(vehicle1, created);
        verify(vehicleRepository).save(vehicle1);
    }

    @Test
    void testUpdateVehicle_success() {
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setVin("VIN999");
        updatedVehicle.setVehicleName("Updated Car");
        updatedVehicle.setFleetId(300);
        updatedVehicle.setDate(Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        updatedVehicle.setAvailabilityStatus(false);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle result = vehicleService.updateVehicle(1, updatedVehicle);

        assertEquals("VIN999", result.getVin());
        assertEquals("Updated Car", result.getVehicleName());
        assertEquals(300, result.getFleetId());
        assertEquals(Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), result.getDate());
        assertFalse(result.getAvailabilityStatus());

        verify(vehicleRepository).findById(1);
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void testUpdateVehicle_notFound() {
        when(vehicleRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> vehicleService.updateVehicle(99, vehicle1));
        assertTrue(ex.getMessage().contains("Vehicle not found with id: 99"));

        verify(vehicleRepository).findById(99);
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testDeleteVehicle_success() {
        when(vehicleRepository.existsById(1)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(1);

        vehicleService.deleteVehicle(1);

        verify(vehicleRepository).existsById(1);
        verify(vehicleRepository).deleteById(1);
    }

    @Test
    void testDeleteVehicle_notFound() {
        when(vehicleRepository.existsById(99)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> vehicleService.deleteVehicle(99));
        assertTrue(ex.getMessage().contains("Vehicle not found with id: 99"));

        verify(vehicleRepository).existsById(99);
        verify(vehicleRepository, never()).deleteById(anyInt());
    }
}
