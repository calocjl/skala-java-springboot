package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
