package com.indifarm.machineryrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "operators")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String district;
    private String experience;
    private boolean available = true;
}
