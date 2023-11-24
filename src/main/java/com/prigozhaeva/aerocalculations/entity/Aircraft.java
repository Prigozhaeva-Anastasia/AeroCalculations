package com.prigozhaeva.aerocalculations.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotBlank(message = "Поле не должно быть пустым")
    @Pattern(regexp = "^$|^[A-Z0-9-]{7,10}$", message = "Поле должно содержать 7-10 цифр, литинских заглавных букв и знак -")
    @Column(name="aircraft_type")
    private String aircraftType;
    @NotNull(message = "Поле не должно быть пустым")
    @Column(name="passenger_capacity")
    private int passengerCapacity;
    @NotNull(message = "Поле не должно быть пустым")
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
