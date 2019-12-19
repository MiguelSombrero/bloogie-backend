
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
public class AccountHandler {
    
    @Autowired
    private AccountService accountService;
    
    public Mono<ServerResponse> listAccounts(ServerRequest request) {
        Flux<Account> accounts = accountService.findAll();
        return ok().contentType(APPLICATION_JSON).body(accounts, Account.class);
    }
    
    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<Account> account = request.bodyToMono(Account.class);
        return accountService.save(account).flatMap(a -> ok().build());
    }
    
    public Mono<ServerResponse> getAccount(ServerRequest request) {
        return accountService.findById(request.pathVariable("id"))
                .flatMap(account -> ok().contentType(APPLICATION_JSON).bodyValue(account))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
}
