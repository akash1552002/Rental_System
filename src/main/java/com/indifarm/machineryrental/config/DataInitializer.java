package com.indifarm.machineryrental.config;

import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.model.User;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Check if the admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {

            // 1. Create the Admin User
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Set a default password
            adminUser.setRole("ADMIN");

            // 2. Create the corresponding Owner profile for the Admin (as a CHC)
            // This is required because your AdminController tries to find an Owner profile.
            Owner chcAdminProfile = new Owner();
            chcAdminProfile.setName("Main CHC Administrator");
            chcAdminProfile.setEmail("admin@chc.gov.in");
            chcAdminProfile.setPhone("1234567890");
            chcAdminProfile.setOwnerType("GOVERNMENT_CHC");
            chcAdminProfile.setKycStatus("VERIFIED"); // Admin is auto-verified

            // 3. Link them together
            chcAdminProfile.setUser(adminUser);
            adminUser.setOwner(chcAdminProfile); // This link is important

            // 4. Save the user. The owner profile will be saved automatically.
            userRepository.save(adminUser);

            System.out.println(">>> Created default ADMIN user with username 'admin' and password 'admin123'");
        }
    }
}