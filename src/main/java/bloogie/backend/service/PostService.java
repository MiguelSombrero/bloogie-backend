
package bloogie.backend.service;

import bloogie.backend.domain.Post;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for providing Post related methods such as 
 * fetching all posts or creating a post. This class uses mongo database
 * through reactive mongo template.
 * 
 * @author miika
 */

@Service
public class PostService {
    
    @Autowired
    private ReactiveMongoTemplate template;
 
    @Autowired
    private AccountService accountService;
    
    /**
     * Fetch all Posts from the database.
     * 
     * @return All posts
     */
    public Flux<Post> findAll() {
        
        // Ei VIELÄ HAE BLOGIA JOHON KYSEINEN POSTAUS LIITTYY
        
        return template.findAll(Post.class)
                .flatMap(blog -> {
                    return accountService.findOne(blog.getAuthor_id()).zipWith(Mono.just(blog), (a, b) -> {
                        b.setAuthor(a);
                        return b;
                    });
                });
    }
    
    
    /**
     * Save POST to database. Before saving, method sets
     * creation time and author for the POST. Author is
     * fetched from the database according to username
     * found in security context
     * 
     * @param post Post to save
     * @return Saved post
     */
    public Mono<Post> save(Mono<Post> post) {
        return accountService.getAuthenticatedUser().zipWith(post, (a, b) -> {
            b.setCreated(new Date());
            b.setAuthor_id(a.getId());
            b.setAuthor(a);
            return b;
            
        }).flatMap(template::save);
    }
    
    /**
     * Fetch Post with specific id from database.
     * 
     * @param id id of post
     * @return Found post
     */
    public Mono<Post> findOne(String id) {
        return template.findById(id, Post.class);
    }
    
    /**
     * Update existing Post.
     * 
     * @param newPost Post with updated info
     * @param id Id of Blog to update
     * @return Updated Blog
     */
    public Mono<Post> update(Mono<Post> newPost, String id) {
        return template.findById(id, Post.class).zipWith(newPost, (o, n) -> {
            o.setTitle((n.getTitle() == null) ? o.getTitle() : n.getTitle());
            o.setContent((n.getContent() == null) ? o.getContent() : n.getContent());
            return o;
            
        }).flatMap(template::save);
    }
    
    /**
     * Delete Post with specific id from database.
     * 
     * @param id Posts id to delete
     * @return Deleted post
     */
    public Mono<Post> delete(String id) {
        return template.findAndRemove(new Query(Criteria.where("id").is(id)), Post.class);
    }
}

