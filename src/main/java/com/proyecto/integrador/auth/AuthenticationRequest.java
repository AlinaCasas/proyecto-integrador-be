package com.proyecto.integrador.auth;

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
public class AuthenticationRequest {

  @NotNull(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotNull(message = "Password is required")
  @Size(min = 8, message = "Password should be at least 8 characters")
  String password;
}
