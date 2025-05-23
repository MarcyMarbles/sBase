package kz.saya.sbasesecurity.Security;

import kz.saya.sbasesecurity.Handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("api")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig {

    private final JwtUtils jwtUtils;
    private final JwtAuthenticationService jwtAuthenticationService;

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(customizer -> {
                    customizer.accessDeniedHandler(customAccessDeniedHandler);
                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/user/public/**",
                                "/api/main/public/**",
                                "/api/scrims/public/**",
                                "/api/teams/public/**",
                                "/api/gamer-profiles/public/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, jwtAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
