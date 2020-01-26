
package bloogie.backend.config;

import bloogie.backend.handler.AccountHandler;
import bloogie.backend.handler.AuthorizationHandlerFilterFunction;
import bloogie.backend.handler.BlogHandler;
import bloogie.backend.handler.PostHandler;
import bloogie.backend.handler.LoginHandler;
import bloogie.backend.handler.ResourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
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
    private PostHandler postHandler;
    
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
     * General functional endpoint for domain objects. Options are to
     * GET one resource, GET all resources, PUT updated resource,
     * POST a new resource or DELETE resource.
     * 
     * @param basePath Basepath for this resource
     * @param handler Handler for this resource
     * @return Router function for given resource
     */
    private static RouterFunction<ServerResponse> resourceRouter(String basePath, ResourceHandler handler) {
        return route().path(basePath, b1 -> b1
                .nest(path("/{id}"), b2 -> b2
                    .GET("", accept(APPLICATION_JSON), handler::getOne)
                    .PUT("", accept(APPLICATION_JSON), handler::update)
                    .DELETE("", handler::delete))
                    //.filter(new AuthorizationHandlerFilterFunction()))
                .GET("", accept(APPLICATION_JSON), handler::list)
                .POST("", handler::create))
                .build();
    }
    
    /**
     * Functional endpoint for Account resource. Base path is /accounts.
     * 
     * @return Router function listening requests for Account resource
     */
    @Bean
    public RouterFunction<ServerResponse> accountRouter() {
        return resourceRouter("/accounts", accountHandler);
    }
    
    /**
     * Functional endpoint for Post resource. Base path is /posts.
     * 
     * @return Router function listening requests for Post resource
     */
    @Bean
    public RouterFunction<ServerResponse> postRouter() {
        return resourceRouter("/posts", postHandler);
    }
    
    /**
     * Functional endpoint for Blog resource. Base path is /blogs.
     * 
     * @return Router function listening requests for Blog resource
     */
    @Bean
    public RouterFunction<ServerResponse> blogRouter() {
        return resourceRouter("/blogs", blogHandler);
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
