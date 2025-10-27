//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.service.MachineService;
//import com.indifarm.machineryrental.service.BookingService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class FarmerController {
//
//    private final MachineService machineService;
//    private final BookingService bookingService;
//
//    public FarmerController(MachineService machineService, BookingService bookingService) {
//        this.machineService = machineService;
//        this.bookingService = bookingService;
//    }
//
//    // URL: http://localhost:8080/farmer/dashboard
//    @GetMapping("/farmer/dashboard")
//    public String showDashboard(Model model) {
//        model.addAttribute("machines", machineService.getAllMachines());
//        model.addAttribute("bookings", bookingService.getAllBookings());
//        return "farmer_dashboard"; // this corresponds to templates/farmer_dashboard.html
//    }
//}
package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.service.MachineService;
import com.indifarm.machineryrental.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    // URL: http://localhost:8080/farmer/dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        // Get the logged-in farmer
        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

        // Get machines and calculate their prices for this farmer
        List<Machine> machines = machineService.getAllMachines();

        // Use a Map to store machine ID and its calculated price
        Map<Long, Double> calculatedPrices = machines.stream()
                .collect(Collectors.toMap(
                        Machine::getId,
                        machine -> bookingService.getCalculatedPrice(machine, farmer)
                ));

        model.addAttribute("machines", machines);
        model.addAttribute("prices", calculatedPrices); // Pass the map to the view
        model.addAttribute("bookings", bookingService.getBookingsByFarmer(farmer));
        model.addAttribute("farmer", farmer);
        return "farmer_dashboard"; // this corresponds to templates/farmer_dashboard.html
    }
}