package com.prigozhaeva.aerocalculations.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"aircraft", "providedServices", "invoices"})
public class Flight {
    @Id
    @Column(name="id")

    Long id;
    @Column(name="flight_number")
    private String flightNumber;
    @Column(name="flight_direction")
    private String flightDirection;
    @Column(name="flight_type")
    private String flightType;
    @Column(name="dep_city")
    private String depCity;
    @Column(name="arr_city")
    private String arrCity;
    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="dep_date")
    private LocalDate depDate;
    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="arr_date")
    private LocalDate arrDate;
    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(name="dep_time")
    private LocalTime depTime;
    @NotNull(message = "Поле не должно быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Column(name="arr_time")
    private LocalTime arrTime;
    @Column(name="luggage_weight")
    private int luggageWeight;
    @Column(name="num_of_adults")
    private int numOfAdults;
    @Column(name="num_of_children")
    private int numOfChildren;
    @Column(name="num_of_babies")
    private int numOfBabies;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "aircraft_tail_number")
    private Aircraft aircraft;
    @ToString.Exclude
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<ProvidedService> providedServices = new ArrayList<>();
    @ToString.Exclude
    @OneToOne
    private Invoice invoice;
}
