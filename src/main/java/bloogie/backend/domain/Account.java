
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author miika
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    private String name;
    
    //@Indexed(unique = true)
    private String username;
    
    //@JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<String> authorities = new ArrayList<>();
    
    public void setAuthority(String authority) {
        this.authorities.add(authority);
    }
    
    public UserDetails toUserDetails() {
        return new User(this.username, this.password, true, true, true, true,
            this.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}

