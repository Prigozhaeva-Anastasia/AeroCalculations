package com.prigozhaeva.aerocalculations.service;


import com.prigozhaeva.aerocalculations.entity.User;

public interface UserService {
    User createUser(String email, String password, String roleName);
    User createOrUpdateUser(User user);
    User findUserByEmail(String email);
}
