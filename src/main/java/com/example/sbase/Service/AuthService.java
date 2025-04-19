package com.example.sbase.Service;

import com.example.sbase.Entity.User;
import com.example.sbase.Security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthService(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    public User getAuthenticatedUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String userName = authentication.getName();
        if (userName == null) {
            return null;
        }
        return userService.getUserByLogin(userName);
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public ResponseEntity<String> authenticate(String login, String password) {
        if (login == null || password == null) {
            return ResponseEntity.status(400).body("Invalid login or password");
        }
        User foundUser = userService.getUserByLogin(login);
        if (foundUser == null) {
            return ResponseEntity.status(401).body("Invalid login or password");
        }
        User authenticatedUser = userService.login(login, password);
        if (authenticatedUser == null) {
            return ResponseEntity.status(401).body("Invalid login or password");
        }
        String token = jwtUtils.generateToken(
                authenticatedUser.getLogin(),
                String.valueOf(authenticatedUser.getId()),
                authenticatedUser.getUsername()
        );
        return ResponseEntity.ok(token);
    }
}
