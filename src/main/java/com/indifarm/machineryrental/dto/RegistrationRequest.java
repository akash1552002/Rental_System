package com.indifarm.machineryrental.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    // User fields
    private String username;
    private String password;
    private String role; // FARMER, OWNER

    // Profile fields
    private String name;
    private String phone;
    private String email;
    private String address;
    private String district;

    // Farmer-specific fields
    private String aadharNumber;
    private Double landSizeInAcres;
}