package kz.saya.sbasesecurity.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final JwtAuthenticationService jwtAuthenticationService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, JwtAuthenticationService jwtAuthenticationService) {
        this.jwtUtils = jwtUtils;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    private boolean isProtectedPath(String path) {
        return path.startsWith("/api/") || path.startsWith("/home");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null && request.getQueryString() != null) {
                String query = request.getQueryString();
                if (query.startsWith("token=")) {
                    authHeader = "Bearer " + query.substring(6);
                }
            }

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        try {
            if (token != null) {
                if (jwtAuthenticationService.validateToken(token)) {
                    Authentication authentication = jwtAuthenticationService.getAuthentication(token);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }else{
                    String path = request.getRequestURI();
                    if(isProtectedPath(path)){
                        response.sendRedirect("/login");
                        return;
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("Token validation failed: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token validation failed");
        }
    }
}
