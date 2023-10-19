package com.proyecto.integrador.modules.common;

import com.proyecto.integrador.modules.users.dto.UserDto;
import com.proyecto.integrador.modules.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Mapper() {
    }

     public User userDtoToUser(UserDto userDto){
            User user = new User();
            user.setUserId(userDto.getUserId());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            return user;
     }

    public UserDto userToUserDto(User user){
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getUserId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            return userDto;
    }
}
