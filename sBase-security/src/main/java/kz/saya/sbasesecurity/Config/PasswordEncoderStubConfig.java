package kz.saya.sbasesecurity.Config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnMissingBean(PasswordEncoder.class)
public class PasswordEncoderStubConfig {
    @Bean
    public PasswordEncoder noopPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
