package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Role;
import com.prigozhaeva.aerocalculations.entity.User;
import com.prigozhaeva.aerocalculations.repository.RoleRepository;
import com.prigozhaeva.aerocalculations.repository.UserRepository;
import com.prigozhaeva.aerocalculations.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(String email, String password) {
        return userRepository.save(User.builder()
                .email(email)
                .password(password)
                .build());
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = findUserByEmail(email);
        Role role = roleRepository.getRoleByName(roleName);
        user.assignRoleToUser(role);
    }
}
