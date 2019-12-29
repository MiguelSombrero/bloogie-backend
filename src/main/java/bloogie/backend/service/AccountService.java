
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    
    public Mono<Account> getAuthenticatedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMap(username -> {
                    return template.findOne(new Query(Criteria.where("username").is(username)), Account.class);
                });
//                .switchIfEmpty(Mono.defer(() -> {
//                    return Mono.error(new UsernameNotFoundException("User Not Found"));
//                }));
    }
}
