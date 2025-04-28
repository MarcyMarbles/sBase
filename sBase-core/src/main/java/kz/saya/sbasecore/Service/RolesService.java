package kz.saya.sbasecore.Service;

import jakarta.annotation.PostConstruct;
import kz.saya.sbasecore.Repositories.RoleRepository;
import kz.saya.sbasecore.Entity.Roles;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        addRoleIfNotExists("ROLE_USER", "User role", true);
        addRoleIfNotExists("ROLE_ADMIN", "Admin role", false);
        addRoleIfNotExists("ROLE_ANONYMOUS", "Anonymous role", false);
        addRoleIfNotExists("ROLE_ASSIGNER", "Assigner role", false);
        addRoleIfNotExists("ROLE_GUEST", "Guest role", false);
    }

    private void addRoleIfNotExists(String name, String description, boolean isDefaultBySystem) {
        if (roleRepository.findByName(name).isEmpty()) {
            Roles role = new Roles(name, description);
            role.setDefaultRole(isDefaultBySystem);
            roleRepository.save(role);
        }
    }

    public Roles getDefaultRole() {
        return roleRepository.findByDefaultRole((true)).orElse(null);
    }

    public Roles setRoleAsDefault(UUID roleId) {
        // Find the role by ID
        Roles role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return null; // Role not found
        }

        Roles currentDefaultRole = getDefaultRole();

        if (currentDefaultRole != null && !currentDefaultRole.getId().equals(roleId)) {
            currentDefaultRole.setDefaultRole(false);
            roleRepository.save(currentDefaultRole);
        }

        role.setDefaultRole(true);
        return roleRepository.save(role);
    }


}
