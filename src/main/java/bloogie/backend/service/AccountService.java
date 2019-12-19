
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Service
public class AccountService {
    
    @Autowired
    ReactiveMongoTemplate template;
 
    public Flux<Account> findAll() {
        return template.findAll(Account.class);
    }
    
    public Mono<Account> save(Mono<Account> account) {
        return template.save(account);
    }
    
    public Mono<Account> findById(String id) {
        return template.findById(id, Account.class);
    }
  
}
