
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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
    private AccountService accountService;
    
    public Flux<Blog> findAll() {
        return template.findAll(Blog.class);
    }
    
    public Mono<Blog> save(Mono<Blog> blog) {
        Mono<Account> account = accountService.getAuthenticatedUser();
        
        Mono<Blog> mergedBlog = account.zipWith(blog, (a, b) -> {
            b.setCreated(new Date());
            b.setAuthor(a);
            return b;
        });
        
        return template.save(mergedBlog);
    }
    
}

