
package bloogie.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String id;
    private Date created;
    private String title;
    private String content;
    
    @Indexed
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String author_id;
    
    @Transient
    private Account author;
    
    @Indexed
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String blog_id;
    
    @Transient
    private Blog blog;
}

