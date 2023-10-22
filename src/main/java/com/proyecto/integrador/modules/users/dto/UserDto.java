package com.proyecto.integrador.modules.users.dto;

import com.proyecto.integrador.modules.users.enums.Roles;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Roles> roles;

}
