package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private BigDecimal tariff;
    @ToString.Exclude
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ProvidedService> providedServices = new ArrayList<>();
    public void setTariff(BigDecimal tariff) {
        this.tariff = tariff.setScale(2, RoundingMode.HALF_UP);
    }
}
