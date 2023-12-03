package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"providedServices"})
public class Service {
    @Id
    @Column(name="id")
    private Long id;
    @NotBlank(message = "Поле не должно быть пустым")
    @Column(name="name")
    private String name;
    @Column(name="service_type")
    private String serviceType;
    @Column(name="tariff")
    private double tariff;
    @ToString.Exclude
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ProvidedService> providedServices = new ArrayList<>();
}
