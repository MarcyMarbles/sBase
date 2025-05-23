package kz.saya.sbaseweb.Controllers.Rest;

import jakarta.servlet.http.HttpServletRequest;
import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbasecore.POJOs.RolePOJO;
import kz.saya.sbasesecurity.Security.JwtUtils;
import kz.saya.sbasecore.Service.RolesService;
import kz.saya.sbasecore.Service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/roles")
public class RolesRestController {

    private final RolesService rolesService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public RolesRestController(RolesService rolesService, JwtUtils jwtUtils, UserService userService) {
        this.rolesService = rolesService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<RolesResponse> getAllRoles(HttpServletRequest request) {
        // Check if user has ADMIN role
        if (!hasAdminRole(request)) {
            return ResponseEntity.status(403).body(new RolesResponse("Access denied. Admin role required.", null));
        }

        List<Roles> roles = rolesService.getAllRoles();

        return ResponseEntity.ok(new RolesResponse("Roles retrieved successfully", roles));
    }

    @PostMapping("/create")
    public ResponseEntity<RoleResponse> createRole(
            HttpServletRequest request,
            @RequestBody RolePOJO rolePOJO
    ) {
        // Check if user has ADMIN role
        if (!hasAdminRole(request)) {
            return ResponseEntity.status(403).body(new RoleResponse("Access denied. Admin role required.", null));
        }

        // Validate input
        if (rolePOJO.name() == null || rolePOJO.description() == null) {
            return ResponseEntity.status(400).body(new RoleResponse("Invalid role data. Name and description are required.", null));
        }

        try {
            Roles role = rolesService.createRole(rolePOJO.name(), rolePOJO.description());
            RolePOJO createdRolePOJO = new RolePOJO(role.getName(), role.getDescription(), role.isDefaultRole());
            return ResponseEntity.ok(new RoleResponse("Role created successfully", createdRolePOJO));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RoleResponse("Error creating role: " + e.getMessage(), null));
        }
    }

    @PostMapping("/set-default/{roleId}")
    public ResponseEntity<RoleResponse> setRoleAsDefault(
            HttpServletRequest request,
            @PathVariable(name = "roleId") String roleId
    ) {
        // Check if user has ADMIN role
        if (!hasAdminRole(request)) {
            return ResponseEntity.status(403).body(new RoleResponse("Access denied. Admin role required.", null));
        }

        try {
            UUID uuid = UUID.fromString(roleId);
            Roles role = rolesService.setRoleAsDefault(uuid);
            if (role == null) {
                return ResponseEntity.status(404).body(new RoleResponse("Role not found", null));
            }
            RolePOJO updatedRolePOJO = new RolePOJO(role.getName(), role.getDescription(), role.isDefaultRole());
            return ResponseEntity.ok(new RoleResponse("Role set as default successfully", updatedRolePOJO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new RoleResponse("Invalid role ID format", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RoleResponse("Error setting role as default: " + e.getMessage(), null));
        }
    }

    private boolean hasAdminRole(HttpServletRequest request) {
        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        String login = jwtUtils.getUsernameFromToken(token);
        if (login == null) {
            return false;
        }

        // Get authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        // Check if user has ADMIN role
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Getter
    @Setter
    public static class RoleResponse {
        private String message;
        private RolePOJO role;

        public RoleResponse(String message, RolePOJO role) {
            this.message = message;
            this.role = role;
        }
    }

    @Getter
    @Setter
    public static class RolesResponse {
        private String message;
        private List<Roles> roles;

        public RolesResponse(String message, List<Roles> roles) {
            this.message = message;
            this.roles = roles;
        }
    }
}
