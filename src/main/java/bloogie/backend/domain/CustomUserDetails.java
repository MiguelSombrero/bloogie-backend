
package bloogie.backend.domain;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author miika
 */

@Data @AllArgsConstructor @NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private String name;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Collection<GrantedAuthority> authorities;
}
