//package com.indifarm.machineryrental.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import com.indifarm.machineryrental.model.Booking;
//
//@Repository
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//}
package com.indifarm.machineryrental.repository;

import com.indifarm.machineryrental.model.Booking;
import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByFarmer(Farmer farmer);
    List<Booking> findByMachineOwner(Owner owner);
}