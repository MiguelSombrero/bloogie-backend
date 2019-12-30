
package bloogie.backend.config;

import bloogie.backend.handler.AccountHandler;
import bloogie.backend.handler.BlogHandler;
import bloogie.backend.handler.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Configuration
@EnableWebFlux
public class DevelopmentWebConfiguration implements WebFluxConfigurer {
    
    @Autowired
    private LoginHandler loginHandler;
    
    @Autowired
    private AccountHandler accountHandler;
    
    @Autowired
    private BlogHandler blogHandler;
    
    @Bean
    public RouterFunction<ServerResponse> loginRouter() {
        return route().POST("/login", loginHandler::login).build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> accountRouter() {
        return route().path("/accounts", builder -> builder
                .GET("/{id}", accept(APPLICATION_JSON), accountHandler::getAccount)
                .GET("", accept(APPLICATION_JSON), accountHandler::listAccounts)
                .POST("", accountHandler::createAccount))
                .build();
    }
    
    @Bean
    public RouterFunction<ServerResponse> blogRouter() {
        return route().path("/blogs", builder -> builder
                .GET("", accept(APPLICATION_JSON), blogHandler::listBlogs)
                .POST("", blogHandler::createBlog))
                .build();
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*");
    }
}
