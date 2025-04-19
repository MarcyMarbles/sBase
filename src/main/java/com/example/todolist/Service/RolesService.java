package com.example.todolist.Service;

import com.example.todolist.Entity.NotPersistent.Langs;
import com.example.todolist.Entity.Roles;
import com.example.todolist.Repos.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolesService {

    private final RoleRepository roleRepository;

    public RolesService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Roles createRole(String name, String description) {
        Roles role = new Roles();
        role.setName(name);
        role.setDescription(description);
        return roleRepository.save(role);
    }

    public Roles getRoleByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public Roles getRoleByDescription(String description) {
        return roleRepository.findByDescription(description).orElse(null);
    }

    public Roles getRoleByNameAndDescription(String name, String description) {
        return roleRepository.findByNameAndDescription(name, description).orElse(null);
    }

    public void deleteRole(Roles roles) {
        roles.setDeleted_ts(OffsetDateTime.now());
        roleRepository.save(roles);
    }

    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @PostConstruct
    public void init() {
        addRoleIfNotExists("ROLE_USER", "User role");
        addRoleIfNotExists("ROLE_ADMIN", "Admin role");
        addRoleIfNotExists("ROLE_ANONYMOUS", "Anonymous role");
        addRoleIfNotExists("ROLE_ASSIGNER", "Assigner role");
        addRoleIfNotExists("ROLE_GUEST", "Guest role");
    }

    private void addRoleIfNotExists(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            roleRepository.save(new Roles(name, description));
        }
    }

    public Set<Roles> getUserRoles(Authentication authentication) {
        Set<Roles> roles = new HashSet<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(roleRepository.findByName(authority.getAuthority()).orElse(null)); // или map к объекту Roles
        }
        return roles;
    }


}
