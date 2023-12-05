package com.prigozhaeva.aerocalculations.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"roles", "employee"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank(message = "Поле не должно быть пустым")
    @Pattern(regexp = "^$|^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,8})+$", message = "Поле должно содержать локальное имя, символ '@' и имя домена(2-8)")
    @Column(name = "email")
    private String email;
    @NotBlank(message = "Поле не должно быть пустым")
    @Pattern(regexp = "$|^(?=.*[A-Z].*[A-Z])(?=.*[0-9])(?=.*[a-z])[0-9a-zA-Z!@#$%^&*/.]{8,}$", message = "Поле должно содержать 2 заглавные латинские буквы латинского, строчные и цифры (>8 симв)")
    @Column(name = "password")
    private String password;
    @NotNull(message = "Поле не должно быть пустым")
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Employee employee;

    public void assignRoleToUser(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }
}
