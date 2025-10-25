package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Operator;
import com.indifarm.machineryrental.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/operator")
public class OperatorController {
    @Autowired
    private OperatorRepository operatorRepo;

    @GetMapping("/dashboard")
    public String operatorDashboard(Model model) {
        model.addAttribute("operators", operatorRepo.findAll());
        return "operator-dashboard";
    }

    @PostMapping("/register")
    public String registerOperator(@ModelAttribute Operator operator) {
        operatorRepo.save(operator);
        return "redirect:/operator/dashboard";
    }
}

