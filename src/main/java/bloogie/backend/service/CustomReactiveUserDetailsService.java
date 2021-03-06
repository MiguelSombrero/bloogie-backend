
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Service for returning customized user details.
 * 
 * @author miika
 */

@Component
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private ReactiveMongoTemplate template;
    
    /**
     * Fetches customized user details. Basically fetches
     * and returns user with given username from database
     * 
     * @param username Username to search for
     * @return UserDetails matching given username
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return template.findOne(new Query(Criteria.where("username").is(username)), Account.class)
                .switchIfEmpty(Mono.defer(() ->  Mono.error(new UsernameNotFoundException("Username not found"))))
                .map(Account::toUserDetails);
    }
    
}
