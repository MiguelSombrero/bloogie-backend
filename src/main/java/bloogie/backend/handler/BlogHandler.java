
package bloogie.backend.handler;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
    
    public Mono<ServerResponse> listBlogs(ServerRequest request) {
        Flux<Blog> blogs = blogService.findAll();
        return ok().contentType(APPLICATION_JSON).body(blogs, Blog.class);
    }
    
    public Mono<ServerResponse> createBlog(ServerRequest request) {
        Mono<Blog> blog = request.bodyToMono(Blog.class);
        return blogService.save(blog).flatMap(a -> ok().build());
    }
}

