//package com.indifarm.machineryrental.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "farmers")
//public class Farmer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String phone;
//    private String email;
//    private String address;
//    private String district;
//}

package com.indifarm.machineryrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "farmers")
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;
    private String district;

    // Fields for subsidy eligibility
    private String aadharNumber;
    private Double landSizeInAcres;
    private boolean isVerified = false; // Default to false, admin can verify

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
