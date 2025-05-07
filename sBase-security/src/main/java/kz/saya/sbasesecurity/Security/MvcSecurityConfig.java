package kz.saya.sbasesecurity.Security;

import kz.saya.sbasesecurity.Service.JwtAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("mvc")
@RequiredArgsConstructor
public class MvcSecurityConfig {

    private final JwtUtils jwtUtils;
    private final JwtAuthenticationService jwtAuthService;

    @Bean
    public SecurityFilterChain mvcSecurity(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(new JwtAuthenticationSuccessHandler(jwtUtils))
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .deleteCookies("JWT_TOKEN", "JSESSIONID")
                .permitAll()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, jwtAuthService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
