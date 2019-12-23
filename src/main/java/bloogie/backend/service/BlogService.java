
package bloogie.backend.service;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.repository.ReactiveAccountRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import static java.time.temporal.TemporalQueries.zone;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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
    private AccountService accountService;
    
    public Flux<Blog> findAll() {
        return template.findAll(Blog.class);
    }
    
    public Mono<Blog> save(Mono<Blog> blog) {
        Mono<Account> account = accountService.getPrincipal();
        
        Mono<Blog> mergedBlog = account.zipWith(blog, (a, b) -> {
            b.setCreated(LocalDateTime.now());
            b.setAuthor(a);
            return b;
        });
        
        return template.save(mergedBlog);
    }
    
}

