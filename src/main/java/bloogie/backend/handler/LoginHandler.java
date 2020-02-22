
package bloogie.backend.handler;

import bloogie.backend.domain.Views;
import bloogie.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Mono;

/**
 * Handler class for login. When a user makes an API call to
 * base path /login, server request is processed in this class.
 * 
 * @author miika
 */

@Component
public class LoginHandler {
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Handler for fetching authenticated user from database
     * and returning it in the body of the server response.
     * 
     * @param request Request received from the client
     * @return Status 200 response with authenticated user in the body 
     */
    public Mono<ServerResponse> login(ServerRequest request) {
        return accountService.getAuthenticatedUser()
                .flatMap(user -> ok().contentType(APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Views.Account.class)
                        .bodyValue(user));
    }
    
    
}

