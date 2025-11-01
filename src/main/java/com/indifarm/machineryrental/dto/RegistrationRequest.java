package com.indifarm.machineryrental.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationRequest {
    // User fields
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "Role is required")
    private String role; // FARMER, OWNER

    // Profile fields
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "District is required")
    private String district;

    // Farmer-specific fields
    @Pattern(regexp = "^\\d{12}$", message = "Aadhar number must be 12 digits")
    private String aadharNumber;
    @PositiveOrZero(message = "Land size must be a positive number")
    private Double landSizeInAcres;
//    private String farmerCategory;

    private String farmerCategory; // <-- ADD THIS
}