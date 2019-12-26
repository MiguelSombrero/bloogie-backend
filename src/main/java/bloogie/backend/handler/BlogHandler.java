
package bloogie.backend.handler;

import bloogie.backend.domain.Blog;
import bloogie.backend.service.BlogService;
import bloogie.backend.validator.BlogValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@Component
public class BlogHandler {
    
    @Autowired
    private BlogValidator validator;
    
    @Autowired
    private BlogService blogService;
    
    public Mono<ServerResponse> listBlogs(ServerRequest request) {
        Flux<Blog> blogs = blogService.findAll();
        return ok().contentType(APPLICATION_JSON).body(blogs, Blog.class);
    }
    
    public Mono<ServerResponse> createBlog(ServerRequest request) {
        Mono<Blog> blog = request.bodyToMono(Blog.class).doOnNext(this::validate);
        return blogService.save(blog).flatMap(b -> ok().build());
    }
    
    private void validate(Blog blog) {
        Errors errors = new BeanPropertyBindingResult(blog, "blog");
        validator.validate(blog, errors);
        
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

