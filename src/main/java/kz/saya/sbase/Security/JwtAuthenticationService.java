package kz.saya.sbase.Security;

import kz.saya.sbase.Entity.User;
import kz.saya.sbase.Service.UserDetailsImpl;
import kz.saya.sbase.Service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public JwtAuthenticationService(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    public Authentication getAuthentication(String token) {
        String username = jwtUtils.extractLogin(token);
        if (username == null) {
            return null;
        }
        
        User user = userService.getUserByLogin(username);
        if (user == null) {
            return null;
        }
        
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return new UsernamePasswordAuthenticationToken(username, token, userDetails.getAuthorities());
    }
    
    public boolean validateToken(String token) {
        try {
            String username = jwtUtils.extractLogin(token);
            return username != null && !jwtUtils.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}