
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private ReactiveMongoTemplate template;
 
    @Autowired
    private PasswordEncoder encoder;
    
    public Flux<Account> findAll() {
        return template.findAll(Account.class);
    }
    
    public Mono<Account> save(Mono<Account> account) {
        return account.map(a -> {
            a.setPassword(encoder.encode(a.getPassword()));
            a.setAuthority("USER");
            return a;
                
        }).flatMap(template::save);
    }
    
    public Mono<Account> findById(String id) {
        return template.findById(id, Account.class);
    }
}
