package kz.saya.sbasesecurity.Service;

import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.Repositories.UserRepository;
import kz.saya.sbasesecurity.Security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserSecurityService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public UserSecurityService(PasswordEncoder passwordEncoder,
                               JwtUtils jwtUtils,
                               UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User extractUserFromToken(String token) {
        token = token.substring(7);
        String login = jwtUtils.getUsernameFromToken(token);
        if (login == null) {
            return null;
        }
        return userRepository.findByLogin(login).orElse(null);
    }

    public User login(String login, String password) {
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
