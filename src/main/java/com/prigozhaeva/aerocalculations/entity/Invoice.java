package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"flight", "employee"})
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String currency;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "flight_number")
    private Flight flight;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
