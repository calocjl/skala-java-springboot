package com.sk.skala.myapp.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.sk.skala.myapp.domain.User;

public class UserServiceProxy extends UserService {

    private final UserService target;
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public UserServiceProxy(UserService target) {
        super(target.getUserRepository());  // 아래 3단계에서 getter 하나 추가할 거예요
        this.target = target;
    }

    @Override
    public List<User> getAllUsers() {
        return logExecutionTime("getAllUsers", () -> target.getAllUsers());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return logExecutionTime("getUserById", () -> target.getUserById(id));
    }

    // 공통 로직: 시작/종료 시각 + 소요시간 출력
    private <T> T logExecutionTime(String methodName, java.util.function.Supplier<T> logic) {
        System.out.println("Proxy " + methodName + " 메소드 시작: " + LocalTime.now().format(TIME_FORMATTER));
        long start = System.currentTimeMillis();

        System.out.println("## 메소드 호출");
        T result = logic.get();   // 진짜 UserService(target)의 메서드 실행

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Proxy " + methodName + " 메소드 종료: "
                + LocalTime.now().format(TIME_FORMATTER) + " 총 소요 시간: " + elapsed + " ms");

        return result;
    }
}
