package com.proyecto.integrador.modules.users.dto;

import jakarta.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UpdateUserDTO {
    @Size(min = 3, max = 50, message = "First name should be between 3 and 50 characters")
    private String firstName;
    @Size(min = 3, max = 50, message = "Last name should be between 3 and 50 characters")
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;
}
