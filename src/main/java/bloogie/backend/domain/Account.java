
package bloogie.backend.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author miika
 */

@Data @Document @AllArgsConstructor @NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private List<String> authorities;
    
    public void addAuthority(String authority) {
        this.authorities.add(authority);
    }
}

