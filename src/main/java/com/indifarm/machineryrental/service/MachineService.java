package com.indifarm.machineryrental.service;

import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.MachineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }
//
public List<Machine> getMachinesByCategory(String category) {
    return machineRepository.findByTypeContainingIgnoreCase(category);
}
    public List<Machine> getChcMachines() {
        return machineRepository.findByOwnerOwnerType("GOVERNMENT_CHC");
    }

    public Optional<Machine> getMachineById(Long id) {
        return machineRepository.findById(id);
    }

    public Machine saveMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    public void deleteMachine(Long id) {
        machineRepository.deleteById(id);
    }

    // --- ADDED SEARCH METHOD ---

    /**
     * Searches for machines based on name and location.
     * Passes null if the search term is empty.
     *
     * @param name The machine name query.
     * @param location The location query.
     * @return A list of matching machines.
     */
    public List<Machine> searchMachines(String name, String location) {
        String nameQuery = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String locationQuery = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        if (nameQuery == null && locationQuery == null) {
            return machineRepository.findAll(); // Return all if no criteria
        }

        return machineRepository.searchMachines(nameQuery, locationQuery);
    }
}