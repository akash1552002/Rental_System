//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.model.User;
//import com.indifarm.machineryrental.service.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class AuthController {
//
//    private final UserService userService;
//
//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String showRegisterPage(Model model) {
//        model.addAttribute("user", new User());
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute User user) {
//        userService.register(user);
//        return "redirect:/login";
//    }
//}

package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.dto.RegistrationRequest;
import com.indifarm.machineryrental.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <-- 1. IMPORT
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute RegistrationRequest registrationRequest) {
//        try {
//            userService.register(registrationRequest);
//            return "redirect:/login?success";
//        } catch (Exception e) {
//            return "redirect:/register?error";
//        }
//    }
@PostMapping("/register")
public String registerUser(@ModelAttribute RegistrationRequest registrationRequest,
                           // --- 2. ADD THIS PARAMETER ---
                           @RequestParam("categoryProofFile") MultipartFile categoryProofFile,
                           RedirectAttributes redirectAttributes) {
    try {
        // --- 3. PASS THE FILE TO THE SERVICE ---
        userService.register(registrationRequest, categoryProofFile);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
        return "redirect:/login";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/register";
    }
}
}