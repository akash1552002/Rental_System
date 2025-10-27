//package com.indifarm.machineryrental.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import com.indifarm.machineryrental.model.Machine;
//
//@Repository
//public interface MachineRepository extends JpaRepository<Machine, Long> {
//}
package com.indifarm.machineryrental.repository;

import com.indifarm.machineryrental.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    // Find all machines owned by CHCs
    List<Machine> findByOwnerOwnerType(String ownerType);
}