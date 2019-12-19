
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Service
public class BlogService {
    
    @Autowired
    ReactiveMongoTemplate template;
 
    public Flux<Blog> findAll() {
        return template.findAll(Blog.class);
    }
    
    public Mono<Blog> save(Mono<Blog> blog) {
        return template.save(blog);
    }
    
}

