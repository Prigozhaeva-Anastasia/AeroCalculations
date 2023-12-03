package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Role;
import com.prigozhaeva.aerocalculations.repository.RoleRepository;
import com.prigozhaeva.aerocalculations.service.RoleService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(String roleName) {
        return roleRepository.save(Role.builder()
                .name(roleName)
                .build());
    }
}
