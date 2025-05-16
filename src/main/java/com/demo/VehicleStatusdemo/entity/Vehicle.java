package com.demo.VehicleStatusdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "VIN must not be blank")
    @Size(min = 3, max = 50, message = "VIN must be between 3 and 50 characters")
    private String vin;

    @NotBlank(message = "Vehicle name must not be blank")
    @Size(min = 2, max = 100, message = "Vehicle name must be between 2 and 100 characters")
    private String vehicleName;

    @NotNull(message = "Availability status must not be null")
    private Boolean availabilityStatus;

    @NotNull(message = "Fleet ID must not be null")
    private Integer fleetId;

    @NotNull(message = "Date must not be null")
    @Temporal(TemporalType.DATE)
    private Date date;
}
