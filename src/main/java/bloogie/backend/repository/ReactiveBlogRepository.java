
package bloogie.backend.repository;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */
public interface ReactiveBlogRepository extends ReactiveMongoRepository<Blog, String> {
    Flux<Blog> findByAuthor(Mono<Account> author);
}
