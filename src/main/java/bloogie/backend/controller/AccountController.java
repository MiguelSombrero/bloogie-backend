
package bloogie.backend.controller;

import bloogie.backend.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import bloogie.backend.repository.ReactiveAccountRepository;

/**
 *
 * @author miika
 */

@RestController
@RequestMapping("/account")
public class AccountController {
    
    @Autowired
    private ReactiveAccountRepository accountRepository;
    
    @GetMapping("/{id}")
    private Mono<Account> getAccount(@PathVariable Long id) {
        return accountRepository.findById(id);
    }
}
