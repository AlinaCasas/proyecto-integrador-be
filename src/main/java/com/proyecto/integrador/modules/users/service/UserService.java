package com.proyecto.integrador.modules.users.service;

import com.proyecto.integrador.common.BadRequestException;
import com.proyecto.integrador.modules.users.dto.UpdateUserDTO;
import com.proyecto.integrador.modules.users.dto.UserDTO;
import com.proyecto.integrador.modules.users.entity.User;
import com.proyecto.integrador.modules.users.repository.UserRepository;
import com.proyecto.integrador.modules.users.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> findUsers(){
        return userRepository.findAllUsers();
    }

    public UserDTO createUser(User user) {
        user.setRoles(List.of(Roles.USER));
        try {
            User saveUser = userRepository.save(user);
            return new UserDTO(saveUser.getId(), saveUser.getFirstName(), saveUser.getLastName(), saveUser.getEmail(), saveUser.getRoles());
        } catch (Exception e) {
            throw new BadRequestException("The email is already in use");
        }
    }

    public UserDTO updateUser(String id, UpdateUserDTO updateUserDTO) {
        String firstName = updateUserDTO.getFirstName();
        String lastName = updateUserDTO.getLastName();
        String email = updateUserDTO.getEmail();
        String password = updateUserDTO.getPassword();

        if (firstName == null && lastName == null && email == null && password == null) throw new BadRequestException("No data to update");

        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getDeletedAt() != null) throw new BadRequestException("The user is deleted, please contact support.");

        if (firstName != null) user.setFirstName(updateUserDTO.getFirstName());
        if (lastName != null) user.setLastName(updateUserDTO.getLastName());
        if (email != null) user.setEmail(updateUserDTO.getEmail());
        if (password != null) user.setPassword(updateUserDTO.getPassword());

        User saveUser = userRepository.save(user);
        return new UserDTO(saveUser.getId(), saveUser.getFirstName(), saveUser.getLastName(), saveUser.getEmail(), saveUser.getRoles());
    }

    public void deleteUser(String id){
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));
        Date deletedAt = new Date();
        user.setDeletedAt(deletedAt);
        userRepository.save(user);
    }
}
