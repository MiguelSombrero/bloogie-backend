
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import bloogie.backend.repository.ReactiveAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private ReactiveAccountRepository accountRepository;
    
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountRepository.findByUsername(username).switchIfEmpty(Mono.defer(() -> {
            return Mono.error(new UsernameNotFoundException("User Not Found"));
        })).map(Account::toUserDetails);
    }
    
}
