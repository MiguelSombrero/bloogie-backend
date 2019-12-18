
package bloogie.backend.config;

import bloogie.backend.handler.AccountHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 *
 * @author miika
 */

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
    
    @Autowired
    private AccountHandler handler;
    
    @Bean
    public RouterFunction<ServerResponse> accountRouter(AccountHandler handler) {
        return route().path("/accounts", builder -> builder
                .GET("/{id}", accept(APPLICATION_JSON), handler::getAccount)
                .GET("", accept(APPLICATION_JSON), handler::listAccounts)
                .POST("", handler::createAccount))
                .build();
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        
    }
}
