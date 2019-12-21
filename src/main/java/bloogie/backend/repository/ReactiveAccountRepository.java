
package bloogie.backend.repository;

import bloogie.backend.domain.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

public interface ReactiveAccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByUsername(String username);
}
