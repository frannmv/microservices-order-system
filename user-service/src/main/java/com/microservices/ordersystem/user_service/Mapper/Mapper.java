package com.microservices.ordersystem.user_service.Mapper;

import com.microservices.ordersystem.user_service.dto.UserDto;
import com.microservices.ordersystem.user_service.model.User;

public class Mapper {

    public static UserDto toDto(User u) {

        if(u == null) return null;

        return new UserDto(
                u.getId(),
                u.getName(),
                u.getEmail()
        );
    }
}
