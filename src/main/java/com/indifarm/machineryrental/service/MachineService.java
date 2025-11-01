package com.indifarm.machineryrental.service;
import com.indifarm.machineryrental.model.Booking; // <-- IMPORT
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.MachineRepository;
import org.springframework.stereotype.Service;
import com.indifarm.machineryrental.repository.BookingRepository; // <-- IMPORT
import java.util.List;
import java.util.Optional;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final BookingRepository bookingRepository; // <-- 1. Make it final and remove "= null"

    // 2. ADD BookingRepository as a parameter to the constructor
    public MachineService(MachineRepository machineRepository, BookingRepository bookingRepository) {
        this.machineRepository = machineRepository;
        this.bookingRepository = bookingRepository; // 3. This now correctly assigns the injected bean
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
        // Find the machine first
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        // Check for associated bookings
        // This will now work correctly
        List<Booking> bookings = bookingRepository.findByMachineOwner(machine.getOwner());
        boolean hasBookings = bookings.stream().anyMatch(b -> b.getMachine().getId().equals(id));

        if (hasBookings) {
            // If bookings exist, throw an exception
            throw new RuntimeException("Cannot delete machine: It has existing bookings.");
        }

        // If no bookings, proceed with deletion
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