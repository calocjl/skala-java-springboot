package com.sk.skala.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sk.skala.myapp.repository.UserRepository;
import com.sk.skala.myapp.service.UserService;
import com.sk.skala.myapp.service.UserServiceProxy;

//@Configuration
public class UserServiceConfig {

    @Bean
    public UserService userService(UserRepository userRepository) {
        // 실제 대상 생성
        UserService target = new UserService(userRepository);
        // 프록시로 감싸서 반환
        return new UserServiceProxy(target);
    }
}