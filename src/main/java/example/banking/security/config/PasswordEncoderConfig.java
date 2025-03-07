package example.banking.security.config;

import example.banking.security.NoOpPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Value("${password.encoder.strength}")
    private int encoderStrength;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            return new NoOpPasswordEncoder(); // In dev, return raw passwords
        } else {
            return new BCryptPasswordEncoder(encoderStrength); // Use real encoding in production
        }
    }
}
