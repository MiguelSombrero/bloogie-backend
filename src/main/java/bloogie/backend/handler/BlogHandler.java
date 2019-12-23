
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.repository.ReactiveAccountRepository;
import bloogie.backend.service.BlogService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
public class BlogHandler {
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private ReactiveAccountRepository accountRepository;
    
    @Autowired
    private ReactiveMongoTemplate template;
    
    public Mono<ServerResponse> listBlogs(ServerRequest request) {
        Flux<Blog> blogs = blogService.findAll();
        return ok().contentType(APPLICATION_JSON).body(blogs, Blog.class);
    }
    
    public Mono<ServerResponse> createBlog(ServerRequest request) {
        Mono<Blog> blog = request.bodyToMono(Blog.class);
        return blogService.save(blog).flatMap(b -> ok().build());
    }
}

