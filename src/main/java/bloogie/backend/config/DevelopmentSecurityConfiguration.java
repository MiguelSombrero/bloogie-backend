
package bloogie.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
@EnableWebFluxSecurity()
public class DevelopmentSecurityConfiguration {
    
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf().disable()
            .exceptionHandling()
		.authenticationEntryPoint((exchange, exception) -> {
                    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                 })
		.accessDeniedHandler((exchange, exception) -> {
                    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                 })
                .and()
            .httpBasic()
                .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
            .formLogin().disable()
            .authorizeExchange()
                .pathMatchers("/accounts").permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers(HttpMethod.GET, "/blogs").permitAll()
                .anyExchange().authenticated()
                .and()
            .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
