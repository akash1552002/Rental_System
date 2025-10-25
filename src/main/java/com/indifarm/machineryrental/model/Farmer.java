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
}
