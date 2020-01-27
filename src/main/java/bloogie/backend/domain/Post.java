
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for Post object. Post is a story with a title and content, which
 * users can write in the Blog.
 * 
 * @author miika
 */

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "posts")
public class Post {
    
    @Id
    @JsonView(Views.Post.class)
    private String id;
    
    @JsonView(Views.Post.class)
    private Date created;
    
    @JsonView(Views.Post.class)
    private String title;
    
    @JsonView(Views.Post.class)
    private String content;
    
    @Indexed
    private String authorId;
    
    @Indexed
    private String blogId;
    
    @Transient
    @JsonView(Views.Post.class)
    private Account author;
    
    @Transient
    @JsonView(Views.Post.class)
    private Blog blog;
}

