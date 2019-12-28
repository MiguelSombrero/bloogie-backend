
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.CustomUserDetails;
import bloogie.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
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
                .map(Account::toUserDetails)
                .flatMap(user -> ok().contentType(APPLICATION_JSON).bodyValue(user));
    }
    
    
}

