package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Role;
import com.prigozhaeva.aerocalculations.entity.User;
import com.prigozhaeva.aerocalculations.repository.RoleRepository;
import com.prigozhaeva.aerocalculations.repository.UserRepository;
import com.prigozhaeva.aerocalculations.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;

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
    public User createUser(String email, String password, String roleName) {
        User user = User.builder()
                .email(email)
                .password(password)
                .roles(new HashSet<>())
                .build();
        Role role = roleRepository.getRoleByName(roleName);
        user.assignRoleToUser(role);
        return userRepository.save(user);
    }

    @Override
    public User createOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
