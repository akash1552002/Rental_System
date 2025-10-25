package com.indifarm.machineryrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.indifarm.machineryrental.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
