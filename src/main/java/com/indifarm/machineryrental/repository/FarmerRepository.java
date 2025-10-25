package com.indifarm.machineryrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.indifarm.machineryrental.model.Farmer;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
}
