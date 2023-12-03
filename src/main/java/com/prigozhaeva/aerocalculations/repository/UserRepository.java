package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
