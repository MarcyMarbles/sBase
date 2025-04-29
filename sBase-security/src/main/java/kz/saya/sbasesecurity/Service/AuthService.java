package kz.saya.sbasesecurity.Service;

import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.Service.UserService;
import kz.saya.sbasesecurity.Security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserSecurityService userSecurityService;

    public AuthService(AuthenticationManager authManager,
                       JwtUtils jwtUtils,
                       UserService userService,
                       UserSecurityService userSecurityService) {
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.userSecurityService = userSecurityService;
    }

    public String login(String username, String password) throws AuthenticationException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return jwtUtils.generateToken(userDetails);
    }

    public User getAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null ||
                !auth.isAuthenticated() ||
                !(auth.getPrincipal() instanceof UserDetailsImpl)) {
            return null;
        }
        String login = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        return userService.getUserByLogin(login);
    }
    public String register(String username, String password) {
        if (userService.isUserAlreadyCreated(username)) {
            throw new IllegalArgumentException("User already exists: " + username);
        }
        User user = new User();
        user.setLogin(username);
        user = userService.createUser(user);

        user = userSecurityService.updatePassword(user, password);

        return login(username, password);
    }


    public LocalDateTime getTokenExpiration(String token) {
        return jwtUtils.getTokenExpiration(token);
    }
}
