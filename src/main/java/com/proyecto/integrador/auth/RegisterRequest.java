package com.proyecto.integrador.auth;

import com.proyecto.integrador.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @NotNull(message = "First name is required")
  @Size(min = 3, max = 50, message = "First name should be between 3 and 50 characters")
  private String firstname;

  @NotNull(message = "Last name is required")
  @Size(min = 3, max = 50, message = "Last name should be between 3 and 50 characters")
  private String lastname;

  @NotNull(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotNull(message = "Password is required")
  @Size(min = 8, message = "Password should be at least 8 characters")
  private String password;

  private Role role;
}
