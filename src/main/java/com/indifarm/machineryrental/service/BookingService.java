package com.indifarm.machineryrental.service;

import com.indifarm.machineryrental.model.Booking;
import com.indifarm.machineryrental.repository.BookingRepository;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired
    private MachineRepository machineRepo;
    @Autowired private FarmerRepository farmerRepo;

    public void createBooking(Long farmerId, Long machineId, LocalDate start, LocalDate end) {
        Booking booking = new Booking();
        booking.setFarmer(farmerRepo.findById(farmerId).orElseThrow());
        booking.setMachine(machineRepo.findById(machineId).orElseThrow());
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setStatus("Pending");
        bookingRepo.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }
}

