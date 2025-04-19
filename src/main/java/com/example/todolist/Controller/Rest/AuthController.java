package com.example.todolist.Controller.Rest;

import com.example.todolist.POJOs.UserAuthPOJO;
import com.example.todolist.Entity.User;
import com.example.todolist.Security.JwtUtils;
import com.example.todolist.Service.UserService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody UserAuthPOJO userAuthPOJO
    ) {
        if (userAuthPOJO.login() == null || userAuthPOJO.password() == null) {
            return ResponseEntity.status(400).body(new TokenResponse("Invalid login.html or password", null));
        }
        User user = userService.login(userAuthPOJO.login(), userAuthPOJO.password());
        if (user == null) {
            return ResponseEntity.status(401).body(new TokenResponse("Invalid login.html or password", null));
        }
        String token = jwtUtils.generateToken(
                user.getLogin(),
                String.valueOf(user.getId()),
                user.getUsername()
        );
        return ResponseEntity.ok(new TokenResponse(token, jwtUtils.getExpirationDate(token)));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(
            @RequestBody UserAuthPOJO userAuthPOJO
    ) {
        if (userAuthPOJO.login() == null || userAuthPOJO.password() == null) {
            return ResponseEntity.status(400).body(new TokenResponse("Invalid login.html or password", null));
        }
        if(userService.isUserAlreadyCreated(userAuthPOJO)) {
            return ResponseEntity.status(400).body(new TokenResponse("User already created", null));
        }
        User user = new User();
        user.setPassword(userAuthPOJO.password());
        user.setLogin(userAuthPOJO.login());
        user = userService.createUser(user);
        String token = jwtUtils.generateToken(
                user.getLogin(),
                String.valueOf(user.getId()),
                user.getUsername()
        );
        return ResponseEntity.ok(new TokenResponse(token, jwtUtils.getExpirationDate(token)));
    }

    @Getter
    public static class TokenResponse {
        private final String token;
        private final LocalDateTime expirationDate;
        private final LocalDate date = LocalDate.now();

        public TokenResponse(String token, LocalDateTime expirationDate) {
            this.token = token;
            this.expirationDate = expirationDate;
        }
    }
}
