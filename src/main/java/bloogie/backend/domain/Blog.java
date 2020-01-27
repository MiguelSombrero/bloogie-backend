
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView({ Views.Blog.class, Views.Account.class, Views.Post.class })
    private String id;
    
    @JsonView({ Views.Account.class, Views.Blog.class, Views.Post.class })
    private String name;
    
    private String authorId;
    
    @Transient
    @JsonView(Views.Blog.class)
    private Account author;
}
