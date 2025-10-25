package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private MachineRepository machineRepo;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("chcMachines", machineRepo.findAll());
        return "admin-dashboard";
    }

    @GetMapping("/add-machine")
    public String addCHCMachinePage() {
        return "add-machine";
    }
}

