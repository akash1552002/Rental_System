package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.service.MachineService;
import com.indifarm.machineryrental.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/farmer")
public class FarmerController {

    private final MachineService machineService;
    private final BookingService bookingService;
    private final FarmerRepository farmerRepository;

    public FarmerController(MachineService machineService, BookingService bookingService, FarmerRepository farmerRepository) {
        this.machineService = machineService;
        this.bookingService = bookingService;
        this.farmerRepository = farmerRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
        List<Machine> machines = machineService.getAllMachines();
        Map<Long, Double> calculatedPrices = machines.stream()
                .collect(Collectors.toMap(
                        Machine::getId,
                        machine -> bookingService.getCalculatedPrice(machine, farmer)
                ));

        model.addAttribute("machines", machines);
        model.addAttribute("prices", calculatedPrices);
        // Bookings are no longer needed here
        // model.addAttribute("bookings", bookingService.getBookingsByFarmer(farmer));
        model.addAttribute("farmer", farmer);
        return "farmer_dashboard";
    }

    @GetMapping("/book/{machineId}")
    public String showBookingPage(@PathVariable("machineId") Long machineId, Model model, Principal principal) {
        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
        Machine machine = machineService.getMachineById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found"));
        double calculatedPrice = bookingService.getCalculatedPrice(machine, farmer);

        model.addAttribute("machine", machine);
        model.addAttribute("calculatedPrice", calculatedPrice);
        model.addAttribute("isSubsidized", calculatedPrice < machine.getRentalPricePerDay());

        return "booking-page";
    }

    // --- ADD THIS NEW METHOD ---
    /**
     * Shows the page listing all the farmer's bookings.
     */
    @GetMapping("/my-bookings")
    public String showMyBookings(Model model, Principal principal) {
        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

        model.addAttribute("bookings", bookingService.getBookingsByFarmer(farmer));

        return "my-bookings"; // Return the new HTML file
    }
    // --- END OF NEW METHOD ---
}

//