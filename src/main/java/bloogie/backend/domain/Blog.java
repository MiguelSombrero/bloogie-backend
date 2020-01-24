
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for Blog object. Blog is a kind of platform which in
 * user can publish a posts.
 * 
 * @author miika
 */

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "blogs")
public class Blog {
    @Id
    private String id;
    private String name;
    
    @Indexed
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String author_id;
    
    @Transient
    private Account author;
    
    @Indexed
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> post_ids = new ArrayList<>();
    
    @Transient
    private List<Post> posts = new ArrayList<>();
    
}
