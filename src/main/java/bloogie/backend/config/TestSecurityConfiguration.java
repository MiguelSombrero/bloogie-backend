
package bloogie.backend.config;

import bloogie.backend.service.CustomReactiveUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 *
 * @author miika
 */

@Profile({"default", "test"})
@EnableWebFluxSecurity
public class TestSecurityConfiguration {
    
    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
            .authorizeExchange()
                .pathMatchers("/accounts").permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers("/css", "/css/*").permitAll()
                .pathMatchers("/h2-console", "/h2-console/**").permitAll()
                .anyExchange().authenticated().and()
            .formLogin()
                .loginPage("/login").and()
            .build();
    }
    
    @Bean
    public MapReactiveUserDetailsService reactiveUserDetailsService() {
        UserDetails user = User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
