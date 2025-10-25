package com.indifarm.machineryrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.indifarm.machineryrental.model.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
