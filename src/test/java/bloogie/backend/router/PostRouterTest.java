
package bloogie.backend.router;

import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import bloogie.backend.domain.Post;
import bloogie.backend.service.PostService;
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
public class PostRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private TestUtils utils;
    
    @MockBean
    private PostService postService;
    
    private Account account;
    private Blog blog;
    private Post post1;
    private Post post2;
    
    @BeforeEach
    public void setUp() {
        this.account = utils.giveUser("oidasajfdlihfaidh", "Jukka Riekkonen", "jukka", "jukka");
        this.blog = utils.giveBlog("asdfuasdfhaosdifhasd", "Parsa blogi", account);
        this.post1 = utils.givePost("sdfjhasdfasd", "New blog", "This is my new blog", account, blog);
        this.post2 = utils.givePost("daufa9u0ouhfoauhdf", "To be or not to be", "That is the guestion", account, blog);
    }
    
    @Test
    @WithMockUser
    public void canAddPostToDatabaseWhenAuthorized() {
        Mockito.when(postService.savePost(any(Mono.class))).thenReturn(Mono.just(post1));
        
        client
                .post().uri("/posts")
                .body(Mono.just(post1), Post.class)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBody()
                    .jsonPath("$.id").isEqualTo("sdfjhasdfasd")
                    .jsonPath("$.title").isEqualTo("New blog")
                    .jsonPath("$.content").isEqualTo("This is my new blog")
                    .jsonPath("$.created").exists()
                    .jsonPath("$.blog.id").isEqualTo("asdfuasdfhaosdifhasd")
                    .jsonPath("$.blog.name").isEqualTo("Parsa blogi")
                    .jsonPath("$.author.id").isEqualTo("oidasajfdlihfaidh")
                    .jsonPath("$.author.name").isEqualTo("Jukka Riekkonen")
                    .jsonPath("$.author.username").isEqualTo("jukka");   
    }
    
    @Test
    public void cannotAddPostToDatabaseIfNotAuthorized() {
        Mockito.when(postService.savePost(any(Mono.class))).thenReturn(Mono.just(post1));
        
        client
                .post().uri("/posts")
                .body(Mono.just(post1), Post.class)
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();   
    }
    
    @Test
    public void canGetPostsFromDatabase() {
        Mockito.when(postService.findAllPosts()).thenReturn(Flux.just(post1, post2));
         
        client
                .get().uri("/posts")
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

