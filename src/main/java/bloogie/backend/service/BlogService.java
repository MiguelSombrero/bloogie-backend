
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.repository.ReactiveAccountRepository;
import java.time.LocalDateTime;
import static java.time.temporal.TemporalQueries.zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ReactiveMongoTemplate template;
 
    @Autowired
    private ReactiveAccountRepository accountRepository;
    
    public Flux<Blog> findAll() {
        return template.findAll(Blog.class);
    }
    
    public Mono<Blog> save(Mono<Blog> blog, Mono<Account> author) {
        return blog.map(b -> {
            b.setCreated(LocalDateTime.now());
            
            author.map(a -> {
                b.setAuthor(a);
                return a;
            });
            
            return b;
            
        }).flatMap(template::save);
    }
    
}

