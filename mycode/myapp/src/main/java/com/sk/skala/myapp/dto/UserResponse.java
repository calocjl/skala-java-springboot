package com.sk.skala.myapp.dto;

import com.sk.skala.myapp.domain.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;

    // Entity(User) → DTO로 변환하는 정적 팩토리 메서드 (4일차 노트 8-2절에서 배운 패턴)
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}