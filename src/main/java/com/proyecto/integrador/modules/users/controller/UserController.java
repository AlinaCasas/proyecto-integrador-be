package com.proyecto.integrador.modules.users.controller;

import com.proyecto.integrador.modules.users.dto.UserDto;
import com.proyecto.integrador.modules.users.entity.User;
import com.proyecto.integrador.modules.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getOne(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    void create(@RequestBody User user) {
        userService.createUser(user);
    }

    @PutMapping("/{id}")
    void update(@PathVariable("id") String id, @RequestBody UserDto user) {
        userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }
}
