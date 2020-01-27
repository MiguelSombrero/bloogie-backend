
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Class for Account object i.e. user registered in this application.
 * Password and authorities are persisted but never serialized to JSON,
 * avoiding them to end up to client.
 * 
 * @author miika
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Document(collection = "accounts")
public class Account {
    
    @Id
    @JsonView({ Views.Account.class, Views.Blog.class, Views.Post.class })
    private String id;
    
    @JsonView({ Views.Account.class, Views.Blog.class, Views.Post.class })
    private String name;
    
    @Indexed(unique = true)
    @JsonView({ Views.Account.class, Views.Blog.class, Views.Post.class })
    private String username;
    
    @Transient
    @JsonView(Views.Account.class)
    private List<Blog> blogs = new ArrayList<>();
    
    private String password;
    private List<String> authorities = new ArrayList<>();
    private List<String> blogIds = new ArrayList<>();
    
    public void setBlogId(String id) {
        this.blogIds.add(id);
    }
    
    public void setAuthority(String authority) {
        this.authorities.add(authority);
    }
    
    public UserDetails toUserDetails() {
        return new User(this.username, this.password, true, true, true, true,
            this.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}

