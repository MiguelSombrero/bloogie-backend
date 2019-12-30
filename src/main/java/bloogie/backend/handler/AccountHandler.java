
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
 * Handler class for Account resource. When a user makes an API call to
 * base path /account, server request is processed in this class.
 * 
 * @author miika
 */

@Component
public class AccountHandler {
    
    @Autowired
    private AccountValidator validator;
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Handler for fetching all accounts and returning
     * them in the body of server response. This handler is activated
     * with GET request to path /accounts.
     * 
     * @param request Request received from the client
     * @return Status 200 response with all in the accounts body
     */
    public Mono<ServerResponse> listAccounts(ServerRequest request) {
        Flux<Account> accounts = accountService.findAll();
        return ok().contentType(APPLICATION_JSON).body(accounts, Account.class);
    }
    
    /**
     * Handler for creating an account and returning
     * it in the body of server response. Handler also validates
     * the Account object received from the request. This handler
     * is activated with POST request to path /account.
     * 
     * @param request Request received from the client
     * @return Status 200 response without a body
     */
    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<Account> account = request.bodyToMono(Account.class).doOnNext(this::validate);
        return accountService.save(account).flatMap(a -> ok().build());
    }
    
    /**
     * Handler for fetching one account and returning
     * it in the body of server response. This handler
     * is activated with GET request to path /account/{id}.
     * 
     * @param request Request received from the client
     * @return Status 200 response with requested account in the body
     */
    public Mono<ServerResponse> getAccount(ServerRequest request) {
        return accountService.findById(request.pathVariable("id"))
                .flatMap(account -> ok().contentType(APPLICATION_JSON).bodyValue(account))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    /**
     * Validates an Account received from the server request. If there
     * is a validation errors, throws new ServerWebInputException.
     * 
     * @param account Account to validate
     */
    private void validate(Account account) {
        Errors errors = new BeanPropertyBindingResult(account, "account");
        validator.validate(account, errors);
        
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
