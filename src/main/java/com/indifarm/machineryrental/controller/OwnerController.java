package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private MachineRepository machineRepo;

    @GetMapping("/dashboard")
    public String ownerDashboard(Model model) {
        model.addAttribute("machines", machineRepo.findAll());
        return "owner-dashboard";
    }

    @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute Machine machine) {
        machine.setOwnershipType("Private");
        machineRepo.save(machine);
        return "redirect:/owner/dashboard";
    }
}

