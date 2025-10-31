package com.indifarm.machineryrental.config;

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.model.User;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.repository.MachineRepository;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final FarmerRepository farmerRepository;
    private final MachineRepository machineRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           OwnerRepository ownerRepository,
                           FarmerRepository farmerRepository,
                           MachineRepository machineRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.farmerRepository = farmerRepository;
        this.machineRepository = machineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // --- 1. Create ADMIN (CHC) User ---
        Owner adminOwner = null;
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole("ADMIN");

            Owner chcAdminProfile = new Owner();
            chcAdminProfile.setName("Main CHC Administrator");
            chcAdminProfile.setEmail("admin@chc.gov.in");
            chcAdminProfile.setPhone("1234567890");
            chcAdminProfile.setOwnerType("GOVERNMENT_CHC");
            chcAdminProfile.setKycStatus("VERIFIED");

            chcAdminProfile.setUser(adminUser);
            adminUser.setOwner(chcAdminProfile);

            userRepository.save(adminUser);
            adminOwner = chcAdminProfile; // Save for later
            System.out.println(">>> Created default ADMIN user with username 'admin'");
        } else {
            adminOwner = ownerRepository.findByUserUsername("admin").orElse(null);
        }

        // --- 2. Create PRIVATE OWNER User ---
        Owner privateOwner = null;
            if (userRepository.findByUsername("ramesh").isEmpty()) {
            User rameshUser = new User();
            rameshUser.setUsername("ramesh");
            rameshUser.setPassword(passwordEncoder.encode("ramesh123"));
            rameshUser.setRole("OWNER");

            Owner rameshProfile = new Owner();
            rameshProfile.setName("Ramesh Sharma");
            rameshProfile.setEmail("ramesh@gmail.com");
            rameshProfile.setPhone("9876543210");
            rameshProfile.setOwnerType("PRIVATE");
            rameshProfile.setKycStatus("PENDING");

            rameshProfile.setUser(rameshUser);
            rameshUser.setOwner(rameshProfile);

            userRepository.save(rameshUser);
            privateOwner = rameshProfile; // Save for later
            System.out.println(">>> Created default OWNER user with username 'ramesh'");
        } else {
            privateOwner = ownerRepository.findByUserUsername("ramesh").orElse(null);
        }


        // --- 3. Create FARMER User ---
        if (userRepository.findByUsername("suresh").isEmpty()) {
            User sureshUser = new User();
            sureshUser.setUsername("suresh");
            sureshUser.setPassword(passwordEncoder.encode("suresh123"));
            sureshUser.setRole("FARMER");

            Farmer sureshProfile = new Farmer();
            sureshProfile.setName("Suresh Patel");
            sureshProfile.setEmail("suresh@gmail.com");
            sureshProfile.setPhone("8877665544");
            sureshProfile.setDistrict("Bhiwandi");
            sureshProfile.setAddress("123, Village Road");
            sureshProfile.setAadharNumber("1234 5678 9012");
            sureshProfile.setLandSizeInAcres(4.5);
            sureshProfile.setVerified(false);

            sureshProfile.setUser(sureshUser);
            sureshUser.setFarmer(sureshProfile);

            userRepository.save(sureshUser);
            System.out.println(">>> Created default FARMER user with username 'suresh'");
        }

        // --- 4. Create Dummy Machines ---
        if (machineRepository.count() == 0) {

            // Create machines for the Private Owner (Ramesh)
            if (privateOwner != null) {
                Machine tractor = new Machine();
                tractor.setName("John Deere 5050D");
                tractor.setType("Tractor");
                tractor.setLocation("Bhiwandi");
                tractor.setRentalPricePerDay(1800.00);
                    tractor.setImageUrl("https://i.imgur.com/vNVmF6N.jpeg");
                tractor.setAvailable(true);
                tractor.setOwner(privateOwner);
                machineRepository.save(tractor);

                Machine cultivator = new Machine();
                cultivator.setName("Mahindra Cultivator");
                cultivator.setType("Cultivator");
                cultivator.setLocation("Kalyan");
                cultivator.setRentalPricePerDay(900.00);
                cultivator.setImageUrl("https://i.imgur.com/A83c1pE.jpeg");
                cultivator.setAvailable(true);
                cultivator.setOwner(privateOwner);
                machineRepository.save(cultivator);

                // --- NEW MACHINES ADDED ---
                Machine trolley = new Machine();
                trolley.setName("Tractor Trolley");
                trolley.setType("Haulage");
                trolley.setLocation("Bhiwandi");
                trolley.setRentalPricePerDay(1200.00);
                trolley.setImageUrl("https://i.imgur.com/gK9qJtq.jpeg");
                trolley.setAvailable(true);
                trolley.setOwner(privateOwner);
                machineRepository.save(trolley);
            }

            // Create a machine for the CHC Admin
            if (adminOwner != null) {
                Machine harvester = new Machine();
                harvester.setName("New Holland TC5.30");
                harvester.setType("Harvester");
                harvester.setLocation("Thane CHC");
                harvester.setRentalPricePerDay(4500.00);
                harvester.setImageUrl("https://i.imgur.com/j70qf9I.jpeg");
                harvester.setAvailable(true);
                harvester.setOwner(adminOwner);
                machineRepository.save(harvester);

                // --- NEW MACHINES ADDED ---
                Machine chaffCutter = new Machine();
                chaffCutter.setName("Chaff Cutter");
                chaffCutter.setType("Livestock");
                chaffCutter.setLocation("Thane CHC");
                chaffCutter.setRentalPricePerDay(300.00);
                chaffCutter.setImageUrl("https://i.imgur.com/v9Fj4jA.jpeg");
                chaffCutter.setAvailable(true);
                chaffCutter.setOwner(adminOwner);
                machineRepository.save(chaffCutter);

                Machine elevator = new Machine();
                elevator.setName("Grain Elevator");
                elevator.setType("Haulage");
                elevator.setLocation("Thane CHC");
                elevator.setRentalPricePerDay(1500.00);
                elevator.setImageUrl("https://i.imgur.com/TqWq7qX.jpeg");
                elevator.setAvailable(true);
                elevator.setOwner(adminOwner);
                machineRepository.save(elevator);
            }
            System.out.println(">>> Created 6 dummy machines");
        }
    }
}