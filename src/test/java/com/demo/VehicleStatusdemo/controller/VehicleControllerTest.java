package com.demo.VehicleStatusdemo.controller;

import com.demo.VehicleStatusdemo.controller.VehicleController;
import com.demo.VehicleStatusdemo.entity.Vehicle;
import com.demo.VehicleStatusdemo.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private Vehicle sampleVehicle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleVehicle = new Vehicle();
        sampleVehicle.setId(1);
        sampleVehicle.setFleetId(10);
        sampleVehicle.setAvailabilityStatus(true);
        sampleVehicle.setVehicleName("Sample Vehicle");
    }

    @Test
    void testTestEndpoint() {
        String result = vehicleController.test();
        assertEquals("hello world", result);
    }

    @Test
    void testGetVehicles_noFilter() {
        when(vehicleService.getAllVehicles(Optional.empty())).thenReturn(List.of(sampleVehicle));
        List<Vehicle> vehicles = vehicleController.getVehicles(Optional.empty());
        assertEquals(1, vehicles.size());
        verify(vehicleService).getAllVehicles(Optional.empty());
    }

    @Test
    void testGetVehicles_withAvailabilityFilter() {
        when(vehicleService.getAllVehicles(Optional.of(true))).thenReturn(List.of(sampleVehicle));
        List<Vehicle> vehicles = vehicleController.getVehicles(Optional.of(true));
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.get(0).getAvailabilityStatus());
        verify(vehicleService).getAllVehicles(Optional.of(true));
    }

//    @Test
//    void testGetVehiclesByAvailabilityAndFleetId() {
//        when(vehicleService.getAllVehiclesByAvailabilityAndFleetId(Optional.of(true), Optional.of(10)))
//                .thenReturn(List.of(sampleVehicle));
//        List<Vehicle> vehicles = vehicleController.getVehiclesByAvailabilityAndFleetId(Optional.of(true), Optional.of(10));
//        assertEquals(1, vehicles.size());
//        verify(vehicleService).getAllVehiclesByAvailabilityAndFleetId(Optional.of(true), Optional.of(10));
//    }

    @Test
    public void testGetVehiclesByAvailabilityAndFleetId(){
        when(vehicleService.getAllVehiclesByAvailabilityAndFleetId(Optional.of(true),Optional.of(10))).thenReturn(List.of(sampleVehicle));
        List<Vehicle> vehicles = vehicleController.getVehiclesByAvailabilityAndFleetId(Optional.of(true),Optional.of(10));
        assertEquals(1,vehicles.size());
        verify(vehicleService).getAllVehiclesByAvailabilityAndFleetId(Optional.of(true),Optional.of(10));
    }

    @Test
    void testCreateVehicle() {
        when(vehicleService.createVehicle(sampleVehicle)).thenReturn(sampleVehicle);
        ResponseEntity<Vehicle> response = vehicleController.createVehicle(sampleVehicle);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleVehicle, response.getBody());
        verify(vehicleService).createVehicle(sampleVehicle);
    }

    @Test
    void testUpdateVehicle() {
        when(vehicleService.updateVehicle(1, sampleVehicle)).thenReturn(sampleVehicle);
        ResponseEntity<Vehicle> response = vehicleController.updateVehicle(1, sampleVehicle);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleVehicle, response.getBody());
        verify(vehicleService).updateVehicle(1, sampleVehicle);
    }

    @Test
    void testDeleteVehicle() {
        doNothing().when(vehicleService).deleteVehicle(1);
        ResponseEntity<Void> response = vehicleController.deleteVehicle(1);
        assertEquals(204, response.getStatusCodeValue());
        verify(vehicleService).deleteVehicle(1);
    }
}
