////package com.indifarm.machineryrental.service;
////
////import com.indifarm.machineryrental.model.Booking;
////import com.indifarm.machineryrental.repository.BookingRepository;
////import com.indifarm.machineryrental.repository.FarmerRepository;
////import com.indifarm.machineryrental.repository.MachineRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDate;
////import java.util.List;
////
////@Service
////public class BookingService {
////
////    @Autowired private BookingRepository bookingRepo;
////    @Autowired
////    private MachineRepository machineRepo;
////    @Autowired private FarmerRepository farmerRepo;
////
////    public void createBooking(Long farmerId, Long machineId, LocalDate start, LocalDate end) {
////        Booking booking = new Booking();
////        booking.setFarmer(farmerRepo.findById(farmerId).orElseThrow());
////        booking.setMachine(machineRepo.findById(machineId).orElseThrow());
////        booking.setStartDate(start);
////        booking.setEndDate(end);
////        booking.setStatus("Pending");
////        bookingRepo.save(booking);
////    }
////
////    public List<Booking> getAllBookings() {
////        return bookingRepo.findAll();
////    }
////}
////
//package com.indifarm.machineryrental.service;
//
//import com.indifarm.machineryrental.model.Booking;
//import com.indifarm.machineryrental.model.Farmer;
//import com.indifarm.machineryrental.model.Machine;
//import com.indifarm.machineryrental.model.Owner;
//import com.indifarm.machineryrental.repository.BookingRepository;
//import com.indifarm.machineryrental.repository.FarmerRepository;
//import com.indifarm.machineryrental.repository.MachineRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//@Service
//public class BookingService {
//
//    @Autowired private BookingRepository bookingRepo;
//    @Autowired private MachineRepository machineRepo;
//    @Autowired private FarmerRepository farmerRepo;
//
//    public void createBooking(Long farmerId, Long machineId, LocalDate start, LocalDate end) {
//        Farmer farmer = farmerRepo.findById(farmerId).orElseThrow(() -> new RuntimeException("Farmer not found"));
//        Machine machine = machineRepo.findById(machineId).orElseThrow(() -> new RuntimeException("Machine not found"));
//        Owner owner = machine.getOwner();
//
//        Booking booking = new Booking();
//        booking.setFarmer(farmer);
//        booking.setMachine(machine);
//        booking.setStartDate(start);
//        booking.setEndDate(end);
//        booking.setStatus("Pending");
//
//        // Calculate Pricing
//        long days = ChronoUnit.DAYS.between(start, end) + 1;
//        double basePrice = machine.getRentalPricePerDay() * days;
//        double subsidy = 0.0;
//        double commission = 0.0;
//
//        // Check for subsidy
//        if ("GOVERNMENT_CHC".equals(owner.getOwnerType()) && isFarmerEligibleForSubsidy(farmer)) {
//            // 40% subsidy for eligible farmers on CHC machines
//            subsidy = basePrice * 0.40;
//        }
//
//        // Check for private owner commission
//        if ("PRIVATE".equals(owner.getOwnerType())) {
//            // 8% platform fee for private owners
//            commission = basePrice * 0.08;
//        }
//
//        booking.setTotalAmount(basePrice);
//        booking.setSubsidyAmount(subsidy);
//        booking.setFinalAmount(basePrice - subsidy);
//        booking.setPlatformCommission(commission);
//
//        bookingRepo.save(booking);
//    }
//
//    public List<Booking> getAllBookings() {
//        return bookingRepo.findAll();
//    }
//
//    public List<Booking> getBookingsByFarmer(Farmer farmer) {
//        return bookingRepo.findByFarmer(farmer);
//    }
//
//    public List<Booking> getBookingsByOwner(Owner owner) {
//        return bookingRepo.findByMachineOwner(owner);
//    }
//
//    public void approveBooking(Long bookingId) {
//        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
//        // TODO: Add logic to check for machine availability conflicts
//        booking.setStatus("Approved");
//        booking.getMachine().setAvailable(false); // Mark machine as unavailable
//        machineRepo.save(booking.getMachine());
//        bookingRepo.save(booking);
//    }
//
//    public void rejectBooking(Long bookingId) {
//        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
//        booking.setStatus("Rejected");
//        bookingRepo.save(booking);
//    }
//
//    // Subsidy Logic from your document
////    public boolean isFarmerEligibleForSubsidy(Farmer farmer) {
////        // Example logic: must be verified and have < 5 acres
////        return farmer.isVerified() && farmer.getLandSizeInAcres() != null && farmer.getLandSizeInAcres() < 5.0;
////    }
//
//    public double getSubsidyRate(Farmer farmer, Machine machine) {
//        // Rule 1: Must be a verified farmer and a CHC machine
//        if (!farmer.isVerified() || !"GOVERNMENT_CHC".equals(machine.getOwner().getOwnerType())) {
//            return 0.0; // No subsidy
//        }
//
//        // Rule 2: Check by Category (New Logic)
//        if ("SMALL_MARGINAL".equals(farmer.getFarmerCategory()) || "SC_ST".equals(farmer.getFarmerCategory())) {
//            return 0.50; // 50% subsidy for these categories
//        }
//
//        // Rule 3: Check by Land Size (Old Logic)
//        if (farmer.getLandSizeInAcres() != null && farmer.getLandSizeInAcres() < 5.0) {
//            return 0.40; // 40% subsidy for small land
//        }
//
//        // Default: No subsidy if no rules match
//        return 0.0;
//    }
//
//    // Helper method to get a calculated price for display
////    public double getCalculatedPrice(Machine machine, Farmer farmer) {
////        if ("GOVERNMENT_CHC".equals(machine.getOwner().getOwnerType()) && isFarmerEligibleForSubsidy(farmer)) {
////            return machine.getRentalPricePerDay() * 0.60; // 40% discount
////        }
////        return machine.getRentalPricePerDay(); // Full price
////    }
//    public double getCalculatedPrice(Machine machine, Farmer farmer) {
//        // Get the discount rate
//        double subsidyRate = getSubsidyRate(farmer, machine);
//
//        if (subsidyRate > 0) {
//            // Apply the discount
//            return machine.getRentalPricePerDay() * (1.0 - subsidyRate);
//        }
//
//        return machine.getRentalPricePerDay(); // Full price
//    }
//}

//
package com.indifarm.machineryrental.service;

import com.indifarm.machineryrental.model.Booking;
import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.repository.BookingRepository;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired private MachineRepository machineRepo;
    @Autowired private FarmerRepository farmerRepo;

    public void createBooking(Long farmerId, Long machineId, LocalDate start, LocalDate end) {
        Farmer farmer = farmerRepo.findById(farmerId).orElseThrow(() -> new RuntimeException("Farmer not found"));
        Machine machine = machineRepo.findById(machineId).orElseThrow(() -> new RuntimeException("Machine not found"));
        Owner owner = machine.getOwner();

        Booking booking = new Booking();
        booking.setFarmer(farmer);
        booking.setMachine(machine);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setStatus("Pending");

        // Calculate Pricing
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        double basePrice = machine.getRentalPricePerDay() * days;
        double subsidy = 0.0;
        double commission = 0.0;

        // --- MODIFIED SUBSIDY LOGIC ---
        // 1. Get the discount rate (e.g., 0.5 for 50%, 0.4 for 40%)
        double subsidyRate = getSubsidyRate(farmer, machine);

        // 2. Calculate the subsidy amount
        if (subsidyRate > 0.0) {
            subsidy = basePrice * subsidyRate;
        }
        // --- END OF MODIFIED LOGIC ---

        // Check for private owner commission (This logic remains the same)
        if ("PRIVATE".equals(owner.getOwnerType())) {
            // 8% platform fee for private owners
            commission = basePrice * 0.08;
        }

        booking.setTotalAmount(basePrice);
        booking.setSubsidyAmount(subsidy);
        booking.setFinalAmount(basePrice - subsidy);
        booking.setPlatformCommission(commission);

        bookingRepo.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public List<Booking> getBookingsByFarmer(Farmer farmer) {
        return bookingRepo.findByFarmer(farmer);
    }

    public List<Booking> getBookingsByOwner(Owner owner) {
        return bookingRepo.findByMachineOwner(owner);
    }

    public void approveBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        // TODO: Add logic to check for machine availability conflicts
        booking.setStatus("Approved");
        booking.getMachine().setAvailable(false); // Mark machine as unavailable
        machineRepo.save(booking.getMachine());
        bookingRepo.save(booking);
    }

    public void rejectBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("Rejected");
        bookingRepo.save(booking);
    }

    // --- REPLACED isFarmerEligibleForSubsidy ---
    /**
     * Calculates the subsidy rate (e.g., 0.4 for 40%) based on new rules.
     * This method REPLACES the old isFarmerEligibleForSubsidy.
     * @return The discount rate (e.g., 0.5 for 50%), or 0.0 if not eligible.
     */
    public double getSubsidyRate(Farmer farmer, Machine machine) {
        // Rule 1: Must be a verified farmer AND a CHC machine
        if (!farmer.isVerified() || !"GOVERNMENT_CHC".equals(machine.getOwner().getOwnerType())) {
            return 0.0; // No subsidy
        }

        // Rule 2: Check by Farmer Category (as discussed)
        // We assume 'farmerCategory' field exists on the Farmer model
        if ("SMALL_MARGINAL".equals(farmer.getFarmerCategory()) || "SC_ST".equals(farmer.getFarmerCategory())) {
            return 0.50; // 50% subsidy for these categories
        }

        // Rule 3: Check by Land Size (Original Logic)
        if (farmer.getLandSizeInAcres() != null && farmer.getLandSizeInAcres() < 5.0) {
            return 0.40; // 40% subsidy for small land (as a fallback)
        }

        // Default: No subsidy if no specific rules match
        return 0.0;
    }


    // --- UPDATED getCalculatedPrice ---
    /**
     * Helper method to get a calculated price for display on the dashboard.
     * It now uses the central getSubsidyRate logic.
     */
    public double getCalculatedPrice(Machine machine, Farmer farmer) {
        // Get the discount rate from our new central logic
        double subsidyRate = getSubsidyRate(farmer, machine);

        if (subsidyRate > 0) {
            // Apply the calculated discount
            return machine.getRentalPricePerDay() * (1.0 - subsidyRate);
        }

        return machine.getRentalPricePerDay(); // Full price
    }
}