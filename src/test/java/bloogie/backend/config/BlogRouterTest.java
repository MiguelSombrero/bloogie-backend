
package bloogie.backend.config;

import bloogie.backend.domain.Blog;
import bloogie.backend.domain.Account;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private ReactiveMongoTemplate template;
    
    @BeforeEach
    public void setUp(ApplicationContext context) {  
        client = WebTestClient
                .bindToApplicationContext(context)
                .apply(springSecurity())
                .build();
        
        template.dropCollection(Blog.class);
    }
    
    @Test
    @WithMockUser("jussi")
    public void canAddBlogToDatabase() {
        Blog blog = new Blog();
        blog.setTitle("Test blog");
        blog.setContent("Story of me life");
        
        client
                .post().uri("/blogs")
                .body(Mono.just(blog), Blog.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();   
    }
    
    @Test
    @WithMockUser
    public void canGetUsersBlogsDatabase() {
        client
                .get().uri("/blogs")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(Blog.class).hasSize(0)
                .returnResult();
    }
    
}

