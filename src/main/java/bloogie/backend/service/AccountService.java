
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for providing user related methods such as 
 * fetching all users or creating a user. This class uses mongo database
 * through reactive mongo template.
 * 
 * @author miika
 */

@Service
public class AccountService {
    
    @Autowired
    private ReactiveMongoTemplate template;
 
    @Autowired
    private PasswordEncoder encoder;
    
    /**
     * Fetch all Accounts from the database.
     * 
     * @return All accounts
     */
    public Flux<Account> findAll() {
        return template.findAll(Account.class);
    }
    
    /**
     * Save Account to database. Before saving, method encrypts
     * users password and sets authority "USER".
     * 
     * @param account Account to save
     * @return Saved account
     */
    public Mono<Account> save(Mono<Account> account) {
        return account.map(a -> {
            a.setPassword(encoder.encode(a.getPassword()));
            a.setAuthority("USER");
            return a;
                
        }).flatMap(template::save);
    }
    
    /**
     * Update existing Account. At the moment, only name and password
     * can be updated
     * 
     * @param newAccount Account with updated info
     * @param id Id of Account to update
     * @return Updated account
     */
    public Mono<Account> update(Mono<Account> newAccount, String id) {
        return template.findById(id, Account.class).zipWith(newAccount, (o, n) -> {
            o.setPassword((n.getPassword() == null) ? o.getPassword() : encoder.encode(n.getPassword()));
            o.setName((n.getName() == null) ? o.getName() : n.getName());
            return o;
            
        }).flatMap(template::save);
    }
    
    /**
     * Fetch Account with specific id from database.
     * 
     * @param id Accounts id
     * @return Found Account
     */
    public Mono<Account> findById(String id) {
        return template.findById(id, Account.class);
    }
    
    /**
     * Fetch authenticated user from database. Method reads username
     * from security context and fetches Account related to that
     * username from database.
     * 
     * @return Authenticated user
     */
    public Mono<Account> getAuthenticatedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMap(username -> {
                    return template.findOne(new Query(Criteria.where("username").is(username)), Account.class);
                });
    }
}
