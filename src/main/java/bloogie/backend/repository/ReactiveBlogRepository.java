
package bloogie.backend.repository;

import bloogie.backend.domain.Blog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @author miika
 */
public interface ReactiveBlogRepository extends ReactiveMongoRepository<Blog, String> {
    
}
