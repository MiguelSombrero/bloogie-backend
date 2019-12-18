
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.repository.ReactiveAccountRepository;
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
    private ReactiveAccountRepository accountRepository;
    
    public Mono<ServerResponse> listAccounts(ServerRequest request) {
        Flux<Account> accounts = accountRepository.findAll();
        return ok().contentType(APPLICATION_JSON).body(accounts, Account.class);
    }
    
    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<Account> account = request.bodyToMono(Account.class);
        return ok().build(accountRepository.save(account));
    }
    
    public Mono<ServerResponse> getAccount(ServerRequest request) {
        return accountRepository.findById(request.pathVariable("id"))
                .flatMap(account -> ok().contentType(APPLICATION_JSON).bodyValue(account))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
}
