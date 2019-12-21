
package bloogie.backend.filter;

import bloogie.backend.domain.Account;
import bloogie.backend.repository.ReactiveAccountRepository;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

public class AuthorizationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
  
    @Autowired
    private ReactiveAccountRepository accountRepository;
    
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> handler) {
        //Mono<Account> account = request.principal().map(Principal::getName).flatMap(accountRepository::findByUsername);
        
        if (false) {
            return ServerResponse.status(FORBIDDEN).build();
        }
        return handler.handle(request);
    }
}
