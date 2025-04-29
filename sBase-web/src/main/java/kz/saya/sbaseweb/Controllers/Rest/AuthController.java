package kz.saya.sbaseweb.Controllers.Rest;

import kz.saya.sbasecore.POJOs.UserAuthPOJO;
import kz.saya.sbasesecurity.Service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserAuthPOJO authRequest) {
        String token = authService.login(authRequest.login(), authRequest.password());

        LocalDateTime expiresAt = authService.getTokenExpiration(token);

        return ResponseEntity.ok(new TokenResponse("Bearer", token, expiresAt));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserAuthPOJO authRequest) {
        authService.register(authRequest.login(), authRequest.password());

        String token = authService.login(authRequest.login(), authRequest.password());
        LocalDateTime expiresAt = authService.getTokenExpiration(token);

        return ResponseEntity.ok(new TokenResponse("Bearer", token, expiresAt));
    }

    @Data
    static class TokenResponse {
        private final String tokenType;
        private final String accessToken;
        private final LocalDateTime expiresAt;
    }
}
