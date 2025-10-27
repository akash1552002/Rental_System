//package com.indifarm.machineryrental.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import com.indifarm.machineryrental.model.Farmer;
//
//@Repository
//public interface FarmerRepository extends JpaRepository<Farmer, Long> {
//}
package com.indifarm.machineryrental.repository;

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import this
import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByUser(User user);
    Optional<Farmer> findByUserUsername(String username);

    // Add this line
    List<Farmer> findByIsVerified(boolean isVerified);
}