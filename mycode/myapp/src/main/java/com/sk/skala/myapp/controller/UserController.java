package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private List<User> users = new ArrayList<>(List.of(
        new User(1L, "alice", "alice@example.com"),
        new User(2L, "bob", "bob@example.com"),
        new User(3L, "charlie", "charlie@example.com")
    ));
    private long userIdCounter = 4;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.setId(userIdCounter++);
        users.add(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        users.removeIf(user -> user.getId() == id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        for (User user : users) {
            if (user.getId() == id) {
                user.setName(updatedUser.getName());
                user.setEmail(updatedUser.getEmail());
                return user;
            }
        }
        return null;
    }
}