
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Service
public class AuthorizationHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private ReactiveMongoTemplate template;
 
    @Override
    public Mono<ServerResponse> filter(ServerRequest sr, HandlerFunction<ServerResponse> hf) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                
                // miksi tässä kohdassa tulee null pointer
               
                .flatMap(username -> template.findOne(new Query(Criteria.where("username").is(username)), Account.class))
                .switchIfEmpty(Mono.defer(() ->  Mono.error(new UsernameNotFoundException("Username not found"))))
                .filter(account -> !account.getId().equalsIgnoreCase(sr.pathVariable("id")))
                .flatMap(account -> hf.handle(sr))
                .switchIfEmpty(Mono.defer(() -> ServerResponse.status(FORBIDDEN).build()));
    }
    
}
