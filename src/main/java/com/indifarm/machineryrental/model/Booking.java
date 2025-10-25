//package com.indifarm.machineryrental.model;
//import jakarta.persistence.Id;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Entity
//@Data
//public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    private Farmer farmer;
//    @ManyToOne private Machine machine;
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private String status; // Pending, Approved, Completed
//
//    @ManyToOne private Operator operator; // optional
//}

package com.indifarm.machineryrental.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Farmer farmer;

    @ManyToOne
    private Machine machine;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // Pending, Approved, Rejected, Completed

    @ManyToOne
    private Operator operator; // optional

    // Fields for pricing and commission
    private Double totalAmount; // Base price
    private Double subsidyAmount; // Amount discounted
    private Double finalAmount; // Amount farmer pays
    private Double platformCommission; // Platform fee for private owners
}
