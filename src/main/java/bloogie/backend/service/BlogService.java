
package bloogie.backend.service;

import bloogie.backend.domain.Blog;
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
 * Service class for providing Blog related methods such as 
 * fetching all blogs or creating a blog. This class uses mongo database
 * through reactive mongo template.
 * 
 * @author miika
 */

@Service
public class BlogService {
    
    @Autowired
    private ReactiveMongoTemplate template;
 
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PostService postService;
    
    /**
     * Fetch all Blogs from the database.
     * 
     * @return All blogs
     */
    public Flux<Blog> findAll() {
        return template.findAll(Blog.class);
    }
    
    /**
     * Save Blog to database. Before saving, method sets
     * creation time and author for the blog. Author is
     * fetched from the database according to username
     * found in security context
     * 
     * @param blog Blog to save
     * @return Saved blog
     */
    public Mono<Blog> save(Mono<Blog> blog) {
        return accountService.getAuthenticatedUser().zipWith(blog, (a, b) -> {
            b.setAuthor_id(a.getId());
            b.setAuthor(a);
            return b;
            
        }).flatMap(template::save);
    }
    
    /**
     * Fetch Blog with specific id from database.
     * 
     * @param id Blogs id
     * @return Found blog
     */
    public Mono<Blog> findOne(String id) {
        return template.findById(id, Blog.class);
    }
    
    /**
     * Update existing Blog. Only name can be updated
     * 
     * @param newBlog Blog with updated info
     * @param id Id of Blog to update
     * @return Updated Blog
     */
    public Mono<Blog> update(Mono<Blog> newBlog, String id) {
        return template.findById(id, Blog.class).zipWith(newBlog, (o, n) -> {
            o.setName((n.getName() == null) ? o.getName() : n.getName());
            return o;
            
        }).flatMap(template::save);
    }
    
    /**
     * Delete Blog with specific id from database.
     * 
     * @param id Blogs id to delete
     * @return Deleted blog
     */
    public Mono<Blog> delete(String id) {
        return template.findAndRemove(new Query(Criteria.where("id").is(id)), Blog.class);
    }
}

