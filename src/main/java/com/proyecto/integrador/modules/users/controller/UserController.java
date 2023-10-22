package com.proyecto.integrador.modules.users.controller;

import com.proyecto.integrador.modules.users.dto.UpdateUserDTO;
import com.proyecto.integrador.modules.users.dto.UserDTO;
import com.proyecto.integrador.modules.users.entity.User;
import com.proyecto.integrador.modules.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> find() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsers());
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody(required = false) @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable(value = "id", required = false) String id, @RequestBody @Valid UpdateUserDTO updateUserDto, Errors errors) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, updateUserDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }
}
