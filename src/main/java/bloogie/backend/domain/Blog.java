
package bloogie.backend.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for Blog object. Blog is a story with a title and content, which
 * users can write in the blog service.
 * 
 * @author miika
 */

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "blogs")
public class Blog {
    @Id
    private String id;
    private Date created;
    private String title;
    private String content;
    private Account author;
}

