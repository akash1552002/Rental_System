package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.service.MachineService;
import com.indifarm.machineryrental.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmerController {

    private final MachineService machineService;
    private final BookingService bookingService;

    public FarmerController(MachineService machineService, BookingService bookingService) {
        this.machineService = machineService;
        this.bookingService = bookingService;
    }

    // URL: http://localhost:8080/farmer/dashboard
    @GetMapping("/farmer/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("machines", machineService.getAllMachines());
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "farmer_dashboard"; // this corresponds to templates/farmer_dashboard.html
    }
}
