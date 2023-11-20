package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airlines")
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude={"aircrafts"})
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="payer_name")
    private String payerName;
    @Column(name="country_code")
    private String countryCode;
    @Column(name="address")
    private String address;
    @Column(name="phone_number")
    private String phone_number;
    @Column(name="email")
    private String email;
    @ToString.Exclude
    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    private List<Aircraft> aircrafts = new ArrayList<>();

    public Airline() {

    }
}
