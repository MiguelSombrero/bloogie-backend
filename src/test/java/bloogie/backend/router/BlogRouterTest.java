
package bloogie.backend.router;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.domain.Post;
import bloogie.backend.service.BlogService;
import bloogie.backend.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@AutoConfigureWebTestClient
@SpringBootTest
public class BlogRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private TestUtils utils;
    
    @MockBean
    private BlogService blogService;
    
    private Account account;
    private Blog blog1;
    private Blog blog2;
    
    @BeforeEach
    public void setUp() {
        this.account = utils.giveUser("oidasajfdlihfaidh", "Jukka Riekkonen", "jukka", "jukka");
        this.blog1 = utils.giveBlog("asdfuasdfhaosdifhasd", "Parsa blogi", account);
        this.blog2 = utils.giveBlog("asdasdfasfdsfd", "Huonoin blogi", account);
    }
    
    @Test
    @WithMockUser
    public void canAddBlogToDatabase() {
        Mockito.when(blogService.saveBlog(any(Mono.class))).thenReturn(Mono.just(blog1));
        
        client
                .post().uri("/blogs")
                .body(Mono.just(blog1), Post.class)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBody()
                    .jsonPath("$.id").isEqualTo("asdfuasdfhaosdifhasd")
                    .jsonPath("$.name").isEqualTo("Parsa blogi")
                    .jsonPath("$.author.id").isEqualTo("oidasajfdlihfaidh")
                    .jsonPath("$.author.name").isEqualTo("Jukka Riekkonen")
                    .jsonPath("$.author.username").isEqualTo("jukka")
                    .jsonPath("$.author.password").doesNotExist();   
    }
    
    @Test
    public void cannotAddBlogToDatabaseIfNotAuthorized() {
        Mockito.when(blogService.saveBlog(any(Mono.class))).thenReturn(Mono.just(blog1));
        
        client
                .post().uri("/blogs")
                .body(Mono.just(blog1), Post.class)
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();   
    }
    
    @Test
    public void canGetBlogsFromDatabase() {
        Mockito.when(blogService.findAllBlogs()).thenReturn(Flux.just(blog1, blog2));
         
        client
                .get().uri("/blogs")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBodyList(Post.class)
                    .hasSize(2);
    }
    
    
}
