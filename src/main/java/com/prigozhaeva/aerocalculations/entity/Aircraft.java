package com.prigozhaeva.aerocalculations.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aircrafts")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude={"airline", "flights"})
public class Aircraft {
    @Id
    @Column(name="tail_number")
    private String tailNumber;
    @Column(name="aircraft_type")
    private String aircraftType;
    @Column(name="passenger_capacity")
    private int passengerCapacity;
    @Column(name="MTOW")
    private int MTOW;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;
    @ToString.Exclude
    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL)
    private List<Flight> flights = new ArrayList<>();
}
