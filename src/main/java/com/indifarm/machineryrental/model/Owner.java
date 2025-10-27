//package com.indifarm.machineryrental.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.util.List;
//
//@Entity
//@Data
//@Table(name = "owners")
//public class Owner {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String phone;
//    private String email;
//    private String address;
//
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//    private List<Machine> machines;
//}

package com.indifarm.machineryrental.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;

    // "PRIVATE" or "GOVERNMENT_CHC"
    private String ownerType;

    // Field for KYC from your document
    private String kycStatus = "PENDING"; // PENDING, VERIFIED

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Machine> machines;
}
