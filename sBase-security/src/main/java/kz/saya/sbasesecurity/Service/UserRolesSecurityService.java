package kz.saya.sbasesecurity.Service;

import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbasecore.Repositories.RoleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserRolesSecurityService {

    private final RoleRepository roleRepository;

    public UserRolesSecurityService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Roles> getUserRoles(Authentication authentication) {
        Set<Roles> roles = new HashSet<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roleRepository.findByName(authority.getAuthority())
                    .ifPresent(roles::add); // <-- исправлено
        }
        return roles;
    }
}
