package com.microservices.ordersystem.user_service.controller;

import com.microservices.ordersystem.user_service.Mapper.Mapper;
import com.microservices.ordersystem.user_service.dto.UserDTO;
import com.microservices.ordersystem.user_service.model.User;
import com.microservices.ordersystem.user_service.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return this.service.getUsers().stream().map(Mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return Mapper.toDto(this.service.getUserById(id));
    }

    @PostMapping
    public UserDTO createUser(@RequestBody User u) {
        return Mapper.toDto(this.service.create(u));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        this.service.delete(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User u) {
        return Mapper.toDto(this.service.update(id,u));
    }
}
