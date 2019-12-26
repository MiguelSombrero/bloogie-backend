
package bloogie.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;

/**
 *
 * @author miika
 */

@Component
@EnableWebFluxSecurity()
public class DevelopmentSecurityConfiguration {
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf().disable()
            .authorizeExchange()
                .pathMatchers("/accounts").permitAll()
                .anyExchange().authenticated()
                .and()
            .httpBasic(withDefaults())
            .formLogin(withDefaults())
            .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
