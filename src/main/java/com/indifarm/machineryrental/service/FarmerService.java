//package com.indifarm.machineryrental.service;
//
//
//import com.indifarm.machineryrental.model.Farmer;
//import com.indifarm.machineryrental.repository.FarmerRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class FarmerService {
//
//    private final FarmerRepository farmerRepository;
//
//    public FarmerService(FarmerRepository farmerRepository) {
//        this.farmerRepository = farmerRepository;
//    }
//
//    public List<Farmer> getAllFarmers() {
//        return farmerRepository.findAll();
//    }
//
//    public Optional<Farmer> getFarmerById(Long id) {
//        return farmerRepository.findById(id);
//    }
//
//    public Farmer saveFarmer(Farmer farmer) {
//        return farmerRepository.save(farmer);
//    }
//
//    public void deleteFarmer(Long id) {
//        farmerRepository.deleteById(id);
//    }
//}
package com.indifarm.machineryrental.service;

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    private final FarmerRepository farmerRepository;

    @Autowired
    public FarmerService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Optional<Farmer> getFarmerById(Long id) {
        return farmerRepository.findById(id);
    }

    public Farmer saveFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public void deleteFarmer(Long id) {
        farmerRepository.deleteById(id);
    }

    // --- New Methods for Verification ---

    /**
     * Finds all farmers who have not yet been verified.
     * @return A list of farmers with isVerified = false
     */
    public List<Farmer> getUnverifiedFarmers() {
        return farmerRepository.findByIsVerified(false);
    }

    /**
     * Verifies a farmer by their ID.
     * @param farmerId The ID of the farmer to verify.
     */
    public void verifyFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + farmerId));

        farmer.setVerified(true); // Set their status to verified
        farmerRepository.save(farmer);
    }
}