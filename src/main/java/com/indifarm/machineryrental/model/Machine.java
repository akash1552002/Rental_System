//package com.indifarm.machineryrental.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "machines")
//public class Machine {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String type;
//    private String description;
//    private double rentalPricePerDay;
//    private String location;
//    private boolean available = true;
//
//    @ManyToOne
//    private Owner owner;
//
//    public void setOwnershipType(String aPrivate) {
//    }
//}
package com.indifarm.machineryrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "machines")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String description;
    private double rentalPricePerDay; // This is the BASE rate
    private String location;
    private boolean available = true;

    @ManyToOne
    private Owner owner;

    // Removed the empty setOwnershipType method.
    // We will get this info from owner.getOwnerType()
}
