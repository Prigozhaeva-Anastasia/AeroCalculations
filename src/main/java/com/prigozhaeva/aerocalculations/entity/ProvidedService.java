package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provided_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"flight"})
public class ProvidedService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "service_id")
    private Service service;
    @Column(name="value")
    private BigDecimal value;
    @Column(name="amount")
    private short amount;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    public void setValue(BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }
}
