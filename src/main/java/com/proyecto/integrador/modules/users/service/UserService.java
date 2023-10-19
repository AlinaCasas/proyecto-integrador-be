package com.proyecto.integrador.modules.users.service;

import com.proyecto.integrador.modules.common.Mapper;
import com.proyecto.integrador.modules.users.dto.UserDto;
import com.proyecto.integrador.modules.users.entity.User;
import com.proyecto.integrador.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private Mapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // Find user by id or email
//    public Optional<User> getUser(Long id, String email){
//        if(id != null){
//            return userRepository.findById(id);
//        }else if(email != null){
//            return Optional.ofNullable(userRepository.findByEmail(email));
//        }else{
//            return null;
//        }
//    }

    public Optional<User> getUserById(String id){
        return userRepository.findById(id);
    }
    public User createUser(User user){
        User user2 = userRepository.save(user);
        System.out.println(user2);

        return user2;
    }

    public User updateUser(String id, UserDto userDto){
        Optional<User> findUser = userRepository.findById(id);

        if(findUser.isEmpty()) {
            return null;
        }

        userDto.setUserId(id);
        if (userDto.getFirstName() == null) {
            userDto.setFirstName(findUser.get().getFirstName());
        }
        if (userDto.getLastName() == null) {
            userDto.setLastName(findUser.get().getLastName());
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(findUser.get().getEmail());
        }
        if (userDto.getPassword() == null) {
            userDto.setPassword(findUser.get().getPassword());
        }
        User user = mapper.userDtoToUser(userDto);
        User saveUser = userRepository.save(user);
        return saveUser;

    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
