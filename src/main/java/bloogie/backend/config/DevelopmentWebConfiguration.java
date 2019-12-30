
package bloogie.backend.config;

import bloogie.backend.handler.AccountHandler;
import bloogie.backend.handler.BlogHandler;
import bloogie.backend.handler.LoginHandler;
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
 * Class for setting web configurations (like cors policy) and defining
 * functional endpoints (routes) for API calls.
 * 
 * Notice that configurations in this class is for development environment.
 * Production environment should usually have its own web configurations
 * with stricter rules.
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
    
    /**
     * Functional endpoint for login. Only POST request to path /login
     * is supported.
     * 
     * @return Router function with response to login request
     */
    @Bean
    public RouterFunction<ServerResponse> loginRouter() {
        return route().POST("/login", loginHandler::login).build();
    }
    
    /**
     * Functional endpoint for Account resource. Base path is /account and
     * the options are to GET one account, GET all accounts or POST a
     * new account. 
     * 
     * @return Router function with response to Account request
     */
    @Bean
    public RouterFunction<ServerResponse> accountRouter() {
        return route().path("/accounts", builder -> builder
                .GET("/{id}", accept(APPLICATION_JSON), accountHandler::getAccount)
                .GET("", accept(APPLICATION_JSON), accountHandler::listAccounts)
                .POST("", accountHandler::createAccount))
                .build();
    }
    
    /**
     * Functional endpoint for Blog resource. Base path is /blog and
     * the options are GET all blogs or POST a new blog.
     * 
     * @return Router function with response to Blog request
     */
    @Bean
    public RouterFunction<ServerResponse> blogRouter() {
        return route().path("/blogs", builder -> builder
                .GET("", accept(APPLICATION_JSON), blogHandler::listBlogs)
                .POST("", blogHandler::createBlog))
                .build();
    }
    
    /**
     * Registers CORS settings to apply application wide. Note that these configurations
     * are set for development environment, and should be re-evaluated when in production.
     * 
     * @param registry Class for registering CORS configurations
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*");
    }
}
