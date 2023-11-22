package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airlines")
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude={"aircrafts"})
public class Airline {
    @Id
    @Column(name="id")
    private Long id;
    @NotBlank(message = "Поле не должно быть пустым")
    @Column(name="name")
    private String name;
    @NotBlank(message = "Поле не должно быть пустым")
    @Column(name="payer_name")
    private String payerName;
    @NotBlank(message = "Поле не должно быть пустым")
    @Pattern(regexp = "^$|^[A-Z][A-Z]$", message = "Поле должно содержать 2 латинские буквы в верхнем регистре")
    @Column(name="country_code")
    private String countryCode;
    @Column(name="address")
    private String address;
    @Pattern(regexp = "^$|^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", message = "Поле должно быть введено в формате +375(29)(44)(33)(25)_______")
    @Column(name="phone_number")
    private String phone_number;
    @NotBlank(message = "Поле не должно быть пустым")
    @Pattern(regexp = "^$|^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,8})+$", message = "Поле должно содержать локальное имя, символ '@' и имя домена(2-8)")
    @Column(name="email")
    private String email;
    @ToString.Exclude
    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    private List<Aircraft> aircrafts = new ArrayList<>();

    public Airline() {

    }
}
