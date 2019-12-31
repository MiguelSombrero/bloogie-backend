
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
            b.setCreated(new Date());
            b.setAuthor(a);
            return b;
            
        }).flatMap(template::save);
    }
    
}

