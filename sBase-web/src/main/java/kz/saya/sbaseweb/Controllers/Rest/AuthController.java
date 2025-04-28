package kz.saya.sbaseweb.Controllers.Rest;

import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.POJOs.UserAuthPOJO;
import kz.saya.sbasesecurity.Security.JwtUtils;
import kz.saya.sbasecore.Service.UserService;
import kz.saya.sbasesecurity.Service.UserSecurityService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final UserService userService;

    private final UserSecurityService userSecurityService;

    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, UserSecurityService userSecurityService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody UserAuthPOJO userAuthPOJO
    ) {
        if (userAuthPOJO.login() == null || userAuthPOJO.password() == null) {
            return ResponseEntity.status(400).body(new TokenResponse("Invalid login.html or password", null));
        }
        User user = userSecurityService.login(userAuthPOJO.login(), userAuthPOJO.password());
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
        if (userService.isUserAlreadyCreated(userAuthPOJO.login())) {
            return ResponseEntity.status(400).body(new TokenResponse("User already created", null));
        }
        User user = new User();
        user.setPassword(userAuthPOJO.password());
        user.setLogin(userAuthPOJO.login());
        user = userService.createUser(user);
        if (user == null) {
            return ResponseEntity.status(400).body(new TokenResponse("User already created", null));
        }
        user = userSecurityService.updatePassword(user, userAuthPOJO.password());
        if (user == null) {
            return ResponseEntity.status(400).body(new TokenResponse("User already created", null));
        }
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
