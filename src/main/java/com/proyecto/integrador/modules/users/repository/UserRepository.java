package com.proyecto.integrador.modules.users.repository;

import com.proyecto.integrador.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
