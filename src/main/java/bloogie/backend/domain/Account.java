
package bloogie.backend.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author miika
 */

@Data @Entity @AllArgsConstructor @NoArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    @NotNull
    @Size(min = 1, max = 20, message = "Name should be between 1-20 character")
    private String name;
    
    @NotNull
    @Size(min = 5, max = 20, message = "Username should be between 5-20 character")
    private String username;
    
    @NotNull
    @Size(min = 8, max = 100, message = "Password should be between 8-100 character")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    private String password;
    
    @Email
    @Size(min = 5, max = 50, message = "Email should be between 5-50 character")
    private String email;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Blog> blogs = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities = new ArrayList<>();
    
    public void addAuthority(String authority) {
        this.authorities.add(authority);
    }
}

