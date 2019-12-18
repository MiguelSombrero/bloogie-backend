
package bloogie.backend.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author miika
 */

@Data @Document @NoArgsConstructor @AllArgsConstructor
public class Blog {
    @Id
    private String id;
    private LocalDateTime created;
    private String title;
    private String content;
    private String author_id;
}

