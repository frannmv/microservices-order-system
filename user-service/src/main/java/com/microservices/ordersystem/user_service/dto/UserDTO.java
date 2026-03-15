package com.microservices.ordersystem.user_service.dto;

import lombok.Getter;

@Getter
public class UserDTO {

    private Long id;
    private String name;
    private String email;

    public UserDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
