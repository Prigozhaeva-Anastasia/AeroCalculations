package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provided_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"flights"})
public class ProvidedService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @Column(name="amount")
    private short amount;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
}
