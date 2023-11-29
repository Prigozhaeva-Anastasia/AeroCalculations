package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "services")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @Column(name="id")
    protected Long id;
    @Column(name="name")
    protected String name;
    @Column(name="service_type")
    protected String serviceType;
    @Column(name="tariff")
    protected double tariff;
}
