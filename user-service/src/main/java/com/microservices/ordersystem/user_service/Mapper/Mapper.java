package com.microservices.ordersystem.user_service.Mapper;

import com.microservices.ordersystem.user_service.dto.UserDTO;
import com.microservices.ordersystem.user_service.model.User;

public class Mapper {

    public static UserDTO toDto(User u) {

        if(u == null) return null;

        return new UserDTO(
                u.getId(),
                u.getName(),
                u.getEmail()
        );
    }
}
