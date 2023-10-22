package com.proyecto.integrador.modules.users.repository;

import com.proyecto.integrador.modules.users.dto.UserDTO;
import com.proyecto.integrador.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT new com.proyecto.integrador.modules.users.dto.UserDTO(u.id, u.firstName, u.lastName, u.email, u.roles) FROM User u WHERE u.deletedAt IS NULL")
    List<UserDTO> findAllUsers();

}
