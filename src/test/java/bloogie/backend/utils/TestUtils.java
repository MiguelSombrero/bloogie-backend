package bloogie.backend.utils;


import bloogie.backend.domain.Account;
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
    
    public Post giveBlog(String id, String title, String content, Account author) {
        Post b = new Post();
        b.setId(id);
        b.setTitle(title);
        b.setContent(content);
        b.setCreated(new Date());
        b.setAuthor(author);
        return b;
    }
}
