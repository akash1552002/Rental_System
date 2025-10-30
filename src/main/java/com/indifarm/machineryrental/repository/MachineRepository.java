package com.indifarm.machineryrental.repository;

import com.indifarm.machineryrental.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    // Find all machines owned by CHCs
    List<Machine> findByOwnerOwnerType(String ownerType);

    // --- ADDED SEARCH METHODS ---

    /**
     * Searches for machines where the name or location contains the query strings.
     * The search is case-insensitive.
     *
     * @param nameQuery The string to search for in machine names.
     * @param locationQuery The string to search for in machine locations.
     * @return A list of matching machines.
     */
    @Query("SELECT m FROM Machine m WHERE " +
            "(:nameQuery IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :nameQuery, '%'))) " +
            "AND (:locationQuery IS NULL OR LOWER(m.location) LIKE LOWER(CONCAT('%', :locationQuery, '%')))")
    List<Machine> searchMachines(@Param("nameQuery") String nameQuery,
                                 @Param("locationQuery") String locationQuery);
}