package kz.saya.sbasesecurity.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.saya.sbasesecurity.Security.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Duration;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        long maxAge = Duration.ofMillis(jwtUtils.getExpirationMs()).getSeconds();

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);               // только по HTTPS
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        String sb = "JWT_TOKEN=" + token +
                "; Max-Age=" + maxAge +
                "; Path=/" +
                "; HttpOnly" +
                "; Secure" +
                "; SameSite=Lax";
        response.setHeader("Set-Cookie", sb);

        response.sendRedirect("/home");
    }
}
