
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for Blog object. Blog is a kind of platform in which
 * user can publish posts.
 * 
 * @author miika
 */

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "blogs")
public class Blog {
    @Id
    private String id;
    private String name;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authorId;
    
    @Transient
    private Account author;
}
