package com.microservices.ordersystem.user_service.controller;

import com.microservices.ordersystem.user_service.Mapper.Mapper;
import com.microservices.ordersystem.user_service.dto.UserDto;
import com.microservices.ordersystem.user_service.model.User;
import com.microservices.ordersystem.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(this.service.getUsers().stream().map(Mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(Mapper.toDto(this.service.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User u) {
        UserDto created = Mapper.toDto(this.service.create(u));
        return ResponseEntity.created(URI.create("/users/"+created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User u) {
        return ResponseEntity.ok(Mapper.toDto(this.service.update(id,u)));
    }
}
