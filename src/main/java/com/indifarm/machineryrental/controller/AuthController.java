package com.indifarm.machineryrental.controller;

import jakarta.validation.Valid; // <-- IMPORT
import com.indifarm.machineryrental.dto.RegistrationRequest;
import com.indifarm.machineryrental.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <-- 1. IMPORT
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult; // <-- IMPORT

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("registrationRequest")) { // Keep existing data on validation error
            model.addAttribute("registrationRequest", new RegistrationRequest());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest,
                               BindingResult bindingResult, // <-- ADDED
                               @RequestParam("categoryProofFile") MultipartFile categoryProofFile,
                               RedirectAttributes redirectAttributes) {

        // 1. Manual check for username uniqueness
        if (userService.findByUsername(registrationRequest.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "username.taken", "Username is already taken");
        }

        // 2. Check for all other validation errors
        if (bindingResult.hasErrors()) {
            // Add the object with errors back to the redirect attributes
            // This ensures the form is re-populated with the user's (invalid) data
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationRequest", bindingResult);
            redirectAttributes.addFlashAttribute("registrationRequest", registrationRequest);
            return "redirect:/register"; // Redirect back to the GET mapping
        }

        // 3. If all is good, proceed with registration
        try {
            userService.register(registrationRequest, categoryProofFile);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }
}