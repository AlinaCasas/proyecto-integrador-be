package com.proyecto.integrador.modules.users.entity;

import com.proyecto.integrador.common.Auditable;
import com.proyecto.integrador.modules.reservations.entity.Reservation;
import com.proyecto.integrador.modules.users.enums.Roles;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Data
@Getter
@Entity
@Table(name = "users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column()
    private String id;

    @Column(nullable = false)
    @NotEmpty(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name should be between 3 and 50 characters")
    private String firstName;

    @Column(nullable = false)
    @NotEmpty(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name should be between 3 and 50 characters")
    private String lastName;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false)
    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

    @Column(nullable = false)
    @Nullable
    private List<Roles> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations;
}
