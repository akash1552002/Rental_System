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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // <-- 1. IMPORT THIS

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired private MachineRepository machineRepo;
    @Autowired private OwnerRepository ownerRepo;
    @Autowired private BookingService bookingService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @GetMapping("/dashboard")
    public String ownerDashboard(Model model, Principal principal) {
        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        model.addAttribute("owner", owner);
        model.addAttribute("machines", owner.getMachines());
        model.addAttribute("bookings", bookingService.getBookingsByOwner(owner));
        model.addAttribute("newMachine", new Machine());
        return "owner-dashboard";
    }

    @PostMapping("/add-machine")
    public String addMachine(@ModelAttribute("newMachine") Machine machine,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes, // <-- 2. ADD THIS
                             Principal principal) throws IOException {

        Owner owner = ownerRepo.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (!imageFile.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = imageFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(imageFile.getInputStream(), filePath);

            machine.setImageUrl("/uploads/" + uniqueFileName);
        } else {
            machine.setImageUrl("/images/default_machine.png");
        }

        machine.setOwner(owner);
        machineRepo.save(machine);

        // --- 3. ADD THIS LINE ---
        redirectAttributes.addFlashAttribute("successMessage", "Machine added successfully!");

        return "redirect:/owner/dashboard";
    }
}