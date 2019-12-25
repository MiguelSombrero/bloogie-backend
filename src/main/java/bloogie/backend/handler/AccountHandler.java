
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.service.AccountService;
import bloogie.backend.validator.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
public class AccountHandler {
    
    @Autowired
    private AccountValidator validator;
    
    @Autowired
    private AccountService accountService;
    
    public Mono<ServerResponse> listAccounts(ServerRequest request) {
        Flux<Account> accounts = accountService.findAll();
        return ok().contentType(APPLICATION_JSON).body(accounts, Account.class);
    }
    
    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<Account> account = request.bodyToMono(Account.class).doOnNext(this::validate);
        return accountService.save(account).flatMap(a -> ok().build());
    }
    
    public Mono<ServerResponse> getAccount(ServerRequest request) {
        return accountService.findById(request.pathVariable("id"))
                .flatMap(account -> ok().contentType(APPLICATION_JSON).bodyValue(account))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    private void validate(Account account) {
        Errors errors = new BeanPropertyBindingResult(account, "account");
        validator.validate(account, errors);
        
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
