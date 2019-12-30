
package bloogie.backend.router;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.service.BlogService;
import bloogie.backend.utils.TestUtils;
import org.junit.Before;
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
        this.blog1 = utils.giveBlog("sdfjhasdfasd", "New blog", "This is my new blog", account);
        this.blog2 = utils.giveBlog("daufa9u0ouhfoauhdf", "To be or not to be", "That is the guestion", account);
    }
    
    @Test
    @WithMockUser
    public void canAddBlogToDatabase() {
        Mockito.when(blogService.save(any(Mono.class))).thenReturn(Mono.just(blog1));
        
        client
                .post().uri("/blogs")
                .body(Mono.just(blog1), Blog.class)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBody()
                    .jsonPath("$.id").isEqualTo("sdfjhasdfasd")
                    .jsonPath("$.title").isEqualTo("New blog")
                    .jsonPath("$.content").isEqualTo("This is my new blog")
                    .jsonPath("$.author.id").isEqualTo("oidasajfdlihfaidh")
                    .jsonPath("$.author.name").isEqualTo("Jukka Riekkonen")
                    .jsonPath("$.author.username").isEqualTo("jukka")
                    .jsonPath("$.author.password").doesNotExist();   
    }
    
    @Test
    public void cannotAddBlogToDatabaseIfNotAuthorized() {
        Mockito.when(blogService.save(any(Mono.class))).thenReturn(Mono.just(blog1));
        
        client
                .post().uri("/blogs")
                .body(Mono.just(blog1), Blog.class)
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();   
    }
    
    @Test
    public void canGetBlogsFromDatabase() {
        Mockito.when(blogService.findAll()).thenReturn(Flux.just(blog1, blog2));
         
        client
                .get().uri("/blogs")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBodyList(Blog.class)
                    .hasSize(2);
    }
    
}

