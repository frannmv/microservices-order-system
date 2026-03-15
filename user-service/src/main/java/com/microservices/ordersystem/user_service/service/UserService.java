package com.microservices.ordersystem.user_service.service;

import com.microservices.ordersystem.user_service.exceptions.UserNotFoundException;
import com.microservices.ordersystem.user_service.model.User;
import com.microservices.ordersystem.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getUsers() {
        return this.repo.findAll();
    }

    public User getUserById(Long id) {
        return this.repo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " was not found"));
    }

    public User create(User u) {
        return this.repo.save(u);
    }

    public void delete(Long id) {
        this.repo.delete(this.getUserById(id));
    }

    @Transactional
    public User update(Long id, User u) {
        if(u == null) return null;

        User old = this.getUserById(id);

        old.setEmail(u.getEmail());
        return old;
    }
}
