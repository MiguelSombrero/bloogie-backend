
package bloogie.backend.handler;

import bloogie.backend.domain.Post;
import bloogie.backend.domain.Views;
import bloogie.backend.service.PostService;
import bloogie.backend.validator.PostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.http.codec.json.Jackson2CodecSupport;
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
 * Handler class for Post resource. When a user makes an API call to
 * base path /post, server request is processed in this class.
 * 
 * @author miika
 */

@Component
public class PostHandler {
    
    @Autowired
    private PostValidator validator;
    
    @Autowired
    private PostService postService;
    
    /**
     * Handler for fetching all posts and returning
     * them in the body of server response. This handler is activated
     * with GET request to path /posts.
     * 
     * @param request Request received from the client
     * @return Status 200 response with all posts in body
     */
    public Mono<ServerResponse> listPosts(ServerRequest request) {
        Flux<Post> posts = postService.findAllPosts();
        return ok().contentType(APPLICATION_JSON)
                .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Views.Post.class)
                .body(posts, Post.class);
    }
    
    /**
     * Handler for creating a post and returning
     * it in the body of server response. Handler also validates
     * the Post object received from the request. This handler
     * is activated with POST request to path /posts.
     * 
     * @param request Request received from the client
     * @return Status 200 response with created post in the body
     */
    public Mono<ServerResponse> createPost(ServerRequest request) {
        Mono<Post> post = request.bodyToMono(Post.class).doOnNext(this::validate);
        
        return postService.savePost(post)
                .flatMap(b -> ok().contentType(APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Views.Post.class)
                        .bodyValue(b));
    }
    
    /**
     * Handler for fetching one post and returning
     * it in the body of server response. This handler
     * is activated with GET request to path /posts/{id}.
     * 
     * @param request Request received from the client
     * @return Status 200 response with requested blog in the body
     */
    public Mono<ServerResponse> getPost(ServerRequest request) {
        return postService.findOnePost(request.pathVariable("id"))
                .flatMap(p -> ok().contentType(APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Views.Post.class)
                        .bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    /**
     * Handler for updating existing post and returning
     * it in the body of server response. This handler
     * is activated with PUT request to path /posts/{id}.
     * 
     * @param request Request received from the client
     * @return Status 201 (created) response with updated post in the body
     */
    public Mono<ServerResponse> updatePost(ServerRequest request) {
        Mono<Post> post = request.bodyToMono(Post.class);
        
        return postService.updatePost(post, request.pathVariable("id"))
                .flatMap(p -> status(201).contentType(APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Views.Post.class)
                        .bodyValue(p));
    }
    
    /**
     * Handler for deleting post. This handler
     * is activated with DELETE request to path /posts/{id}.
     * 
     * @param request Request received from the client
     * @return Status 204 (no content) response
     */
    public Mono<ServerResponse> deletePost(ServerRequest request) {
        return postService.deletePost(request.pathVariable("id"))
                .flatMap(p -> noContent().build());
    }
    
    /**
     * Validates a Post received from the server request. If there
     * is a validation errors, throws new ServerWebInputException.
     * 
     * @param post Post to validate
     */
    private void validate(Post post) {
        Errors errors = new BeanPropertyBindingResult(post, "post");
        validator.validate(post, errors);
        
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

