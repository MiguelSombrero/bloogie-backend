
package bloogie.backend.handler;

import bloogie.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
public class LoginHandler {
    
    @Autowired
    private AccountService accountService;
    
    public Mono<ServerResponse> login(ServerRequest request) {
        return accountService.getAuthenticatedUser()
                .flatMap(user -> ok().contentType(APPLICATION_JSON).bodyValue(user));
    }
    
    
}

