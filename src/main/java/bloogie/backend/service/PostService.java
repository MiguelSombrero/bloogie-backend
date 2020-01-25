
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
    private BlogService blogService;
    
    /**
     * Fetch Blog that is related to given Post. Assigns
     * that blog to post and returns it.
     * 
     * @param post Post to populate with blog
     * @return Post
     */
    private Mono<Post> addBlogToPost(Post post) {
        return blogService.findOneBlog(post.getBlogId())
                .zipWith(Mono.just(post), (b, p) -> {
                    p.setBlog(b);
                    return p;
                });
    }
    
    /**
     * Fetch all Posts from the database. Populates each post with the
     * blog it relates to
     * 
     * @return All posts
     */
    public Flux<Post> findAllPosts() {
        return template.findAll(Post.class).flatMap(this::addBlogToPost);
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
    public Mono<Post> savePost(Mono<Post> post) {
        return post.map(p -> {
            p.setCreated(new Date());
            return p;
            
        }).flatMap(template::save);
    }
    
    /**
     * Fetch Post with specific id from database.
     * 
     * @param id id of post
     * @return Found post
     */
    public Mono<Post> findOnePost(String id) {
        return template.findById(id, Post.class);
    }
    
    /**
     * Update existing Post.
     * 
     * @param newPost Post with updated info
     * @param id Id of Blog to update
     * @return Updated Blog
     */
    public Mono<Post> updatePost(Mono<Post> newPost, String id) {
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
    public Mono<Post> deletePost(String id) {
        return template.findAndRemove(new Query(Criteria.where("id").is(id)), Post.class);
    }
}

