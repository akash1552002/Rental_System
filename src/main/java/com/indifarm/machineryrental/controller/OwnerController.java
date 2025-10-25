//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.model.Machine;
//import com.indifarm.machineryrental.repository.MachineRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/owner")
//public class OwnerController {
//    @Autowired
//    private MachineRepository machineRepo;
//
//    @GetMapping("/dashboard")
//    public String ownerDashboard(Model model) {
//        model.addAttribute("machines", machineRepo.findAll());
//        return "owner-dashboard";
//    }
//
//    @PostMapping("/add-machine")
//    public String addMachine(@ModelAttribute Machine machine) {
//        machine.setOwnershipType("Private");
//        machineRepo.save(machine);
//        return "redirect:/owner/dashboard";
//    }
//}
//

package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.repository.MachineRepository;
import com.indifarm.machineryrental.repository.OwnerRepository;
import com.indifarm.machineryrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired private MachineRepository machineRepo;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private BookingService bookingService;

    @GetMapping("/dashboard")
    public String ownerDashboard(Model model, Principal principal) {
        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        model.addAttribute("owner", owner);
        model.addAttribute("machines", owner.getMachines());
        model.addAttribute("bookings", bookingService.getBookingsByOwner(owner));
        model.addAttribute("newMachine", new Machine()); // For the "add machine" form
        return "owner-dashboard";
    }

    @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute Machine machine, Principal principal) {
        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        machine.setOwner(owner); // Link machine to the logged-in owner
        machineRepo.save(machine);

        return "redirect:/owner/dashboard";
    }
}