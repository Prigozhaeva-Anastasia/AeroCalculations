package com.prigozhaeva.aerocalculations.service;


import com.prigozhaeva.aerocalculations.entity.User;

public interface UserService {
    User createUser(String email, String password);
    void assignRoleToUser(String email, String roleName);
    User findUserByEmail(String email);
}
