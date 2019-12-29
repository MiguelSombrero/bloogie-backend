package bloogie.backend.config;


import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 *
 * @author miika
 */

@Component
public class TestUtils {
    
    @Autowired
    private ReactiveMongoTemplate template;
    
    public String createStringOfLength(int length) {
        StringBuilder string = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            string.append("a");
        }
        
        return string.toString();
    }
    
    public String createAuthenticationToken(String username, String password) {
        String passphrase = username + ":" + password;
        return Base64.getEncoder().encodeToString(passphrase.getBytes());
    }
    
    public Mono<Account> saveUser(String name, String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setName(name);
        return template.save(account);
    }
    
    public Mono<Account> giveUser(String name, String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setName(name);
        return Mono.just(account);
    }
    
    public void saveBlog(String title, String content) {
        Blog post = new Blog();
        post.setTitle(title);
        post.setContent(content);
        post.setCreated(new Date());
        template.save(post);
    }
}
