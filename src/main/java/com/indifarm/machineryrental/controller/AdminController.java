////package com.indifarm.machineryrental.controller;
////
////import com.indifarm.machineryrental.repository.MachineRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////
////@Controller
////@RequestMapping("/admin")
////public class AdminController {
////    @Autowired
////    private MachineRepository machineRepo;
////
////    @GetMapping("/dashboard")
////    public String adminDashboard(Model model) {
////        model.addAttribute("chcMachines", machineRepo.findAll());
////        return "admin-dashboard";
////    }
////
////    @GetMapping("/add-machine")
////    public String addCHCMachinePage() {
////        return "add-machine";
////    }
////}
////
//
//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.model.Owner;
//import com.indifarm.machineryrental.repository.OwnerRepository;
//import com.indifarm.machineryrental.service.BookingService;
//import com.indifarm.machineryrental.service.MachineService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    @Autowired private MachineService machineService;
//    @Autowired private OwnerRepository ownerRepo;
//    @Autowired private BookingService bookingService;
//
//    @GetMapping("/dashboard")
//    public String adminDashboard(Model model, Principal principal) {
//        // We assume the "ADMIN" user is linked to a "GOVERNMENT_CHC" Owner profile
//        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));
//
//        model.addAttribute("adminName", adminOwner.getName());
//        model.addAttribute("chcMachines", adminOwner.getMachines());
//        model.addAttribute("bookings", bookingService.getBookingsByOwner(adminOwner));
//        return "admin-dashboard";
//    }
//
//    @GetMapping("/add-machine")
//    public String addCHCMachinePage() {
//        return "add-machine";
//    }
//}
package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.service.BookingService;
import com.indifarm.machineryrental.service.FarmerService; // Import this
import com.indifarm.machineryrental.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Import this
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Import this

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private MachineService machineService;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private BookingService bookingService;
    @Autowired private FarmerService farmerService; // Inject FarmerService

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Principal principal) {
        // We assume the "ADMIN" user is linked to a "GOVERNMENT_CHC" Owner profile
        Owner adminOwner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin owner profile not found"));

        model.addAttribute("adminName", adminOwner.getName());
        model.addAttribute("chcMachines", adminOwner.getMachines());
        model.addAttribute("bookings", bookingService.getBookingsByOwner(adminOwner));

        // Add the list of unverified farmers to the model
        model.addAttribute("unverifiedFarmers", farmerService.getUnverifiedFarmers());

        return "admin-dashboard";
    }

    @GetMapping("/add-machine")
    public String addCHCMachinePage() {
        return "add-machine";
    }

    // Add this new method to handle the verification POST request
    @PostMapping("/verify-farmer")
    public String verifyFarmer(@RequestParam("farmerId") Long farmerId) {
        farmerService.verifyFarmer(farmerId);
        return "redirect:/admin/dashboard";
    }
}