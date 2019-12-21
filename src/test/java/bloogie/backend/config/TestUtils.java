package bloogie.backend.config;


import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.repository.ReactiveAccountRepository;
import bloogie.backend.repository.ReactiveBlogRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 *
 * @author miika
 */

@Component
public class TestUtils {
    
    @Autowired
    private ReactiveAccountRepository accountRepository;
    
    @Autowired
    private ReactiveBlogRepository blogRepository;
    
    @Autowired
    private ReactiveMongoTemplate template;
    
    public String createStringOfLength(int length) {
        StringBuilder string = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            string.append("a");
        }
        
        return string.toString();
    }
    
    public Flux<Account> getUsers() {
        return accountRepository.findAll();
    }
    
    public Flux<Blog> getBlogs() {
        return blogRepository.findAll();
    }
    
    public Mono<Account> saveUser(String name, String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setName(name);
        return accountRepository.save(account);
    }
    
    public void saveBlog(String title, String content) {
        Blog post = new Blog();
        post.setTitle(title);
        post.setContent(content);
        post.setCreated(LocalDateTime.now());
        blogRepository.save(post);
    }
}
