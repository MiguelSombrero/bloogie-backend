
package bloogie.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
                .pathMatchers("/account").permitAll()
                .anyExchange().authenticated()
                .and()
            .httpBasic(withDefaults())
            .formLogin(withDefaults())
            .build();
    }
    
//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails user = User
//                .withUsername("admin")
//                .password(encoder.encode("admin"))
//                .roles("ADMIN")
//                .build();
//        return new MapReactiveUserDetailsService(user);
//    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
