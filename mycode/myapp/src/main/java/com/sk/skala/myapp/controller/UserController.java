package com.sk.skala.myapp.controller;

import lombok.extern.slf4j.Slf4j;
import com.sk.skala.myapp.domain.User;
import com.sk.skala.myapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import com.sk.skala.myapp.dto.UserRequest;
import com.sk.skala.myapp.dto.UserResponse;
import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 모든 사용자 조회
    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("getAllUsers called");
        return userService.getAllUsers();
    }

    // GET: 특정 사용자 가져오기
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        log.debug("getUserById called with id: {}", id);
        return userService.getUserById(id).orElse(null);
    }

    // POST: 사용자 추가 (DTO + 검증 적용된 버전만 남김)
    @PostMapping("/users")
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        User user = userService.createUser(request);
        return UserResponse.from(user);
    }

    // DELETE: 사용자 삭제
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // PUT: 사용자 정보 수정
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser).orElse(null);
    }
}