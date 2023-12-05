package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Role;

import java.util.List;

public interface RoleService {
    Role createRole(String roleName);
    List<Role> fetchAll();
}
