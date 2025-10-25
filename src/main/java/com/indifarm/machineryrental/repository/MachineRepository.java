package com.indifarm.machineryrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.indifarm.machineryrental.model.Machine;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
}
