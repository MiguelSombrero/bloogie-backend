
package bloogie.backend.handler;

import bloogie.backend.domain.Blog;
import bloogie.backend.service.BlogService;
import bloogie.backend.validator.BlogValidator;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Handler class for Blog resource. When a user makes an API call to
 * base path /blog, server request is processed in this class.
 * 
 * @author miika
 */

@Component
public class BlogHandler {
    
    @Autowired
    private BlogValidator validator;
    
    @Autowired
    private BlogService blogService;
    
    /**
     * Handler for fetching all blogs and returning
     * them in the body of server response. This handler is activated
     * with GET request to path /blogs.
     * 
     * @param request Request received from the client
     * @return Status 200 response with all blogs in body
     */
    public Mono<ServerResponse> listBlogs(ServerRequest request) {
        Flux<Blog> blogs = blogService.findAll();
        return ok().contentType(APPLICATION_JSON).body(blogs, Blog.class);
    }
    
    /**
     * Handler for creating a blog and returning
     * it in the body of server response. Handler also validates
     * the Blog object received from the request. This handler
     * is activated with POST request to path /blog.
     * 
     * @param request Request received from the client
     * @return Status 200 response with created blog in the body
     */
    public Mono<ServerResponse> createBlog(ServerRequest request) {
        Mono<Blog> blog = request.bodyToMono(Blog.class).doOnNext(this::validate);
        return blogService.save(blog).flatMap(b -> ok().contentType(APPLICATION_JSON).bodyValue(b));
    }
    
    /**
     * Handler for fetching one blog and returning
     * it in the body of server response. This handler
     * is activated with GET request to path /blogs/{id}.
     * 
     * @param request Request received from the client
     * @return Status 200 response with requested blog in the body
     */
    public Mono<ServerResponse> getBlog(ServerRequest request) {
        return blogService.findOne(request.pathVariable("id"))
                .flatMap(blog -> ok().contentType(APPLICATION_JSON).bodyValue(blog))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    /**
     * Handler for updating existing blog and returning
     * it in the body of server response. This handler
     * is activated with PUT request to path /blogs/{id}.
     * 
     * @param request Request received from the client
     * @return Status 201 (created) response with updated blog in the body
     */
    public Mono<ServerResponse> updateBlog(ServerRequest request) {
        Mono<Blog> blog = request.bodyToMono(Blog.class);
        
        return blogService.update(blog, request.pathVariable("id"))
                .flatMap(b -> status(201).contentType(APPLICATION_JSON).bodyValue(b));
    }
    
    /**
     * Handler for deleting blog. This handler
     * is activated with DELETE request to path /blogs/{id}.
     * 
     * @param request Request received from the client
     * @return Status 204 (no content) response
     */
    public Mono<ServerResponse> deleteBlog(ServerRequest request) {
        return blogService.delete(request.pathVariable("id"))
                .flatMap(b -> noContent().build());
    }
    
    /**
     * Validates an Blog received from the server request. If there
     * is a validation errors, throws new ServerWebInputException.
     * 
     * @param blog Blog to validate
     */
    private void validate(Blog blog) {
        Errors errors = new BeanPropertyBindingResult(blog, "blog");
        validator.validate(blog, errors);
        
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

