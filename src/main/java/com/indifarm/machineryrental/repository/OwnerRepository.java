//package com.indifarm.machineryrental.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import com.indifarm.machineryrental.model.Owner;
//
//@Repository
//public interface OwnerRepository extends JpaRepository<Owner, Long> {
//}
package com.indifarm.machineryrental.repository;

import com.indifarm.machineryrental.model.Owner;
import com.indifarm.machineryrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUser(User user);
    Optional<Owner> findByUserUsername(String username);
}