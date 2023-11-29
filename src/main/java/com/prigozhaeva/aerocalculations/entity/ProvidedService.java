package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provided_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"flights"})
public class ProvidedService extends  Service {
    @Column(name="amount")
    private short amount;
    @ToString.Exclude
    @ManyToMany(mappedBy = "providedServices")
    private List<Flight> flights = new ArrayList<>();
}
