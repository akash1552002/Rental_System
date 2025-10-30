//package com.indifarm.machineryrental.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Controller
//public class HomeController {
//
//    private final ResourceLoader resourceLoader;
//    private final ObjectMapper objectMapper;
//
//    // This will hold our detailed map: "Land_Preparation_Equipment" -> ["Plough", "Subsoiler", ...]
//    private Map<String, List<String>> machineryCategories;
//
//    // This map provides the icons for the main categories
//    private Map<String, String> categoryIcons;
//
//    public HomeController(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
//        this.resourceLoader = resourceLoader;
//        this.objectMapper = objectMapper;
//    }
//
//    /**
//     * This method runs once when the controller is created.
//     * It reads the machinery-types.json file and stores the data.
//     */
//    @PostConstruct
//    public void loadMachineryTypes() {
//        // 1. Initialize the Icon Map
//        categoryIcons = Map.ofEntries(
//                Map.entry("Land_Preparation_Equipment", "🌱"),
//                Map.entry("Sowing_And_Planting_Equipment", "🌽"),
//                Map.entry("Irrigation_Equipment", "💧"),
//                Map.entry("Crop_Protection_Equipment", "🌿"),
//                Map.entry("Harvesting_Equipment", "🌾"),
//                Map.entry("Threshing_And_PostHarvest_Equipment", "🌾"),
//                Map.entry("Tractors_And_Power_Units", "🚜"),
//                Map.entry("Material_Handling_Equipment", "🧺"),
//                Map.entry("Weeding_And_Intercultural_Equipment", "🌿"),
//                Map.entry("Livestock_And_Dairy_Equipment", "🐄"),
//                Map.entry("Storage_And_Processing_Equipment", "🏠"),
//                Map.entry("Modern_And_Smart_Farming_Equipment", "⚡")
//        );
//
//        // 2. Load the JSON data
//        try {
//            Resource resource = resourceLoader.getResource("classpath:static/data/machinery-types.json");
//            InputStream inputStream = resource.getInputStream();
//
//            // Read the outer JSON object: {"Farm_Machinery_And_Equipment": {...}}
//            TypeReference<Map<String, Map<String, List<String>>>> typeRef = new TypeReference<>() {};
//            Map<String, Map<String, List<String>>> outerMap = objectMapper.readValue(inputStream, typeRef);
//
//            // We only care about the inner map
//            machineryCategories = outerMap.get("Farm_Machinery_And_Equipment");
//
//            System.out.println(">>> Successfully loaded " + machineryCategories.size() + " main categories from JSON.");
//
//        } catch (Exception e) {
//            System.err.println("!!! FAILED to load machinery-types.json: " + e.getMessage());
//            e.printStackTrace();
//            machineryCategories = Map.of(); // Set to empty to avoid crashes
//        }
//    }
//
//    @GetMapping("/")
//    public String homePage(Model model) {
//        // Add the data to the model for Thymeleaf to use
//
//        // 1. A Set of the main category names (e.g., "Land_Preparation_Equipment", ...)
//        model.addAttribute("mainCategories", machineryCategories.keySet());
//
//        // 2. The map of icons ("Land_Preparation_Equipment" -> "🌱")
//        model.addAttribute("categoryIcons", categoryIcons);
//
//        // 3. The *entire* data structure, for our JavaScript to use
//        model.addAttribute("allCategoriesData", machineryCategories);
//
//        return "index";
//    }
//
//    @GetMapping("/search")
//    public String searchResults(@RequestParam(value = "machinery", required = false) String machinery,
//                                @RequestParam(value = "location", required = false) String location,
//                                Model model) {
//
//        model.addAttribute("machineryQuery", machinery);
//        model.addAttribute("locationQuery", location);
//
//        return "search-results";
//    }
//}

package com.indifarm.machineryrental.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indifarm.machineryrental.dto.CategoryDTO;
import com.indifarm.machineryrental.service.MachineService; // Import MachineService
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final MachineService machineService; // <-- ADDED

    // This list will hold all our category objects
    private List<CategoryDTO> categories;

    public HomeController(ResourceLoader resourceLoader, ObjectMapper objectMapper, MachineService machineService) { // <-- MODIFIED
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.machineService = machineService; // <-- ADDED
    }

    /**
     * This method runs once when the controller is created.
     * It reads the categories.json file and stores the data.
     */
    @PostConstruct
    public void loadCategories() {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/data/categories.json");
            InputStream inputStream = resource.getInputStream();

            // Read the JSON and convert it to a List of CategoryDTO objects
            categories = objectMapper.readValue(inputStream, new TypeReference<List<CategoryDTO>>() {});

            System.out.println(">>> Successfully loaded " + categories.size() + " categories from JSON.");

        } catch (Exception e) {
            System.err.println("!!! FAILED to load categories.json: " + e.getMessage());
            e.printStackTrace();
            categories = List.of(); // Set to empty to avoid crashes
        }
    }

    @GetMapping("/")
    public String homePage(Model model) {
        // Add the entire list of categories to the model
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/search")
    public String searchResults(@RequestParam(value = "machinery", required = false) String machinery,
                                @RequestParam(value = "location", required = false) String location,
                                Model model) {

        model.addAttribute("machineryQuery", machinery);
        model.addAttribute("locationQuery", location);

        // --- MODIFIED ---
        // Call the service to get machines from the database
        model.addAttribute("machines", machineService.searchMachines(machinery, location));
        // --- END ---

        return "search-results";
    }
}