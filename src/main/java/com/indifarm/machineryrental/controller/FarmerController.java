package com.indifarm.machineryrental.controller;
import com.fasterxml.jackson.core.type.TypeReference; // <-- IMPORT
import com.fasterxml.jackson.databind.ObjectMapper; // <-- IMPORT
import com.indifarm.machineryrental.dto.CategoryDTO;
import jakarta.annotation.PostConstruct; // <-- IMPORT
import org.springframework.core.io.Resource; // <-- IMPORT
import org.springframework.core.io.ResourceLoader;
import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.service.MachineService;
import com.indifarm.machineryrental.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // <-- IMPORT
import java.io.InputStream; // <-- IMPORT
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

    //
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private List<CategoryDTO> categories;

//    public FarmerController(MachineService machineService, BookingService bookingService, FarmerRepository farmerRepository, ResourceLoader resourceLoader, ObjectMapper objectMapper) {
//        this.machineService = machineService;
//        this.bookingService = bookingService;
//        this.farmerRepository = farmerRepository;
//        this.resourceLoader = resourceLoader;
//        this.objectMapper = objectMapper;
//    }
public FarmerController(MachineService machineService, BookingService bookingService,
                        FarmerRepository farmerRepository, ResourceLoader resourceLoader,
                        ObjectMapper objectMapper) {
    this.machineService = machineService;
    this.bookingService = bookingService;
    this.farmerRepository = farmerRepository;
    this.resourceLoader = resourceLoader; // <-- ADD
    this.objectMapper = objectMapper; // <-- ADD
}

    @PostConstruct
    public void loadCategories() {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/data/categories.json");
            InputStream inputStream = resource.getInputStream();
            categories = objectMapper.readValue(inputStream, new TypeReference<List<CategoryDTO>>() {});
            System.out.println(">>> FarmerController loaded " + categories.size() + " categories.");
        } catch (Exception e) {
            e.printStackTrace();
            categories = List.of();
        }
    }
//    @GetMapping("/dashboard")
//    public String showDashboard(Model model, Principal principal) {
//        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
//        List<Machine> machines = machineService.getAllMachines();
//        Map<Long, Double> calculatedPrices = machines.stream()
//                .collect(Collectors.toMap(
//                        Machine::getId,
//                        machine -> bookingService.getCalculatedPrice(machine, farmer)
//                ));
//
//        model.addAttribute("machines", machines);
//        model.addAttribute("prices", calculatedPrices);
//        // Bookings are no longer needed here
//        // model.addAttribute("bookings", bookingService.getBookingsByFarmer(farmer));
//        model.addAttribute("farmer", farmer);
//        return "farmer_dashboard";
//    }
@GetMapping("/dashboard")
public String showDashboard(@RequestParam(value = "category", required = false) String category, // <-- Add category param
                            Model model, Principal principal) {

    Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

    // Get machines based on category
    List<Machine> machines;
    if (category != null && !category.isEmpty()) {
        machines = machineService.getMachinesByCategory(category);
    } else {
        machines = machineService.getAllMachines(); // Default: show all
    }

    Map<Long, Double> calculatedPrices = machines.stream()
            .collect(Collectors.toMap(
                    Machine::getId,
                    machine -> bookingService.getCalculatedPrice(machine, farmer)
            ));

    model.addAttribute("machines", machines);
    model.addAttribute("prices", calculatedPrices);
    model.addAttribute("farmer", farmer);
    model.addAttribute("categories", categories); // <-- Pass categories to the page

    return "farmer_dashboard";
}

    @GetMapping("/book/{machineId}")
    public String showBookingPage(@PathVariable("machineId") Long machineId, Model model, Principal principal) {
        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
        Machine machine = machineService.getMachineById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found"));
        double calculatedPrice = bookingService.getCalculatedPrice(machine, farmer);

        model.addAttribute("machine", machine);
        model.addAttribute("calculatedPrice", calculatedPrice);
        model.addAttribute("isSubsidized", calculatedPrice < machine.getRentalPricePerDay());

        return "booking-page";
    }

    // --- ADD THIS NEW METHOD ---
    /**
     * Shows the page listing all the farmer's bookings.
     */
    @GetMapping("/my-bookings")
    public String showMyBookings(Model model, Principal principal) {
        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

        model.addAttribute("bookings", bookingService.getBookingsByFarmer(farmer));

        return "my-bookings"; // Return the new HTML file
    }
    // --- END OF NEW METHOD ---
}

//