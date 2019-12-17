
package bloogie.backend.repository;

import bloogie.backend.domain.Blog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @author miika
 */
public interface ReactiveBlogRepository extends ReactiveCrudRepository<Blog, Long> {
    Flux<Blog> findTop100ByOrderByCreatedDesc();
}
