package com.microservices.ordersystem.order_service.dto.external;

import lombok.Getter;

@Getter
public class UserDto {

    private Long id;
    private String name;
    private String status;

    public UserDto(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}
