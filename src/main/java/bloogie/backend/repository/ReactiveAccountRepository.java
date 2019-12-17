
package bloogie.backend.repository;

import bloogie.backend.domain.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */
public interface ReactiveAccountRepository extends ReactiveCrudRepository<Account, Long> {
    Mono<Account> findByUsername(String username);
}
