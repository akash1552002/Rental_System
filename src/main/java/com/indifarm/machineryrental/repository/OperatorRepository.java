package com.indifarm.machineryrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.indifarm.machineryrental.model.Operator;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
}
