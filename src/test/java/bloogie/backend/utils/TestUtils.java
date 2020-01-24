package bloogie.backend.utils;


import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.domain.Post;
import java.util.Base64;
import java.util.Date;
import org.springframework.stereotype.Component;


/**
 *
 * @author miika
 */

@Component
public class TestUtils {
    
    public String createAuthenticationToken(String username, String password) {
        String passphrase = username + ":" + password;
        return Base64.getEncoder().encodeToString(passphrase.getBytes());
    }
    
    public Account giveUser(String id, String name, String username, String password) {
        Account a = new Account();
        a.setId(id);
        a.setUsername(username);
        a.setPassword(password);
        a.setName(name);
        return a;
    }
    
    public Blog giveBlog(String name, Account author) {
        Blog b = new Blog();
        b.setName(name);
        return b;
    }
    
    public Post givePost(String id, String title, String content, Account author, Blog blog) {
        Post p = new Post();
        p.setId(id);
        p.setTitle(title);
        p.setContent(content);
        p.setCreated(new Date());
        p.setAuthor(author);
        p.setBlog(blog);
        return p;
    }
}
