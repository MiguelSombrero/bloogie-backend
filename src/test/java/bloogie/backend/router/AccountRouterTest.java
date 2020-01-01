
package bloogie.backend.router;

import bloogie.backend.domain.Account;
import bloogie.backend.service.AccountService;
import bloogie.backend.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
public class AccountRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private TestUtils utils;
    
    @MockBean
    private AccountService accountService;
    
    private Account account1;
    private Account account2;
    
    @BeforeEach
    public void setUp() {
        this.account1 = utils.giveUser("skdjhfasldfh", "Miika", "miika", "miika");
        this.account2 = utils.giveUser("oidasajfdlihfaidh", "Jukka Riekkonen", "jukka", "jukka");
    }
    
    @Test
    public void canAddUserToDatabase() {
        Mockito.when(accountService.save(any(Mono.class))).thenReturn(Mono.just(account1));
                
        client
                .post().uri("/accounts")
                .body(Mono.just(account1), Account.class)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .isEmpty();   
    }
    
    @Test
    @WithMockUser
    public void canGetUsersFromDatabaseWhenAuthenticated() {
        Mockito.when(accountService.findAll()).thenReturn(Flux.just(account1, account2));
         
        client
                .get().uri("/accounts")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBodyList(Account.class)
                    .hasSize(2);
    }
    
    @Test
    public void cannotGetUsersFromDatabaseWhenNotAuthenticated() {
        Mockito.when(accountService.findAll()).thenReturn(Flux.just(account1, account2));
         
        client
                .get().uri("/accounts")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();
    }
    
    @Test
    @WithMockUser
    public void canGetUserByIdFromDatabase() {
        Mockito.when(accountService.findOne("oidasajfdlihfaidh")).thenReturn(Mono.just(account2));
         
        client
                .get().uri("/accounts/oidasajfdlihfaidh")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBody()
                    .jsonPath("$.id").isEqualTo("oidasajfdlihfaidh")
                    .jsonPath("$.name").isEqualTo("Jukka Riekkonen")
                    .jsonPath("$.username").isEqualTo("jukka")
                    .jsonPath("$.password").doesNotExist();
    }
    
    @Test
    public void cannotGetUserByIdFromDatabaseIfNotAuthorized() {
        Mockito.when(accountService.findOne("oidasajfdlihfaidh")).thenReturn(Mono.just(account2));
         
        client
                .get().uri("/accounts/{id}", "oidasajfdlihfaidh")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();
    }
    
    @Test
    @WithMockUser
    public void canUpdateUserWhenAuthenticated() {
        Mockito.when(accountService.update(any(Mono.class), eq("oidasajfdlihfaidh"))).thenReturn(Mono.just(account2));
         
        client
                .put().uri("/accounts/{id}", "oidasajfdlihfaidh")
                .accept(APPLICATION_JSON)
                .body(Mono.just(account1), Account.class)
                .exchange()
                .expectStatus()
                    .isCreated()
                .expectHeader()
                    .contentType(APPLICATION_JSON)
                .expectBody()
                    .jsonPath("$.id").isEqualTo("oidasajfdlihfaidh")
                    .jsonPath("$.name").isEqualTo("Jukka Riekkonen")
                    .jsonPath("$.username").isEqualTo("jukka")
                    .jsonPath("$.password").doesNotExist();
    }
    
    @Test
    public void cannotUpdateUserIfNotAuthorized() {
        Mockito.when(accountService.update(any(Mono.class), eq("oidasajfdlihfaidh"))).thenReturn(Mono.just(account2));
         
        client
                .put().uri("/accounts/oidasajfdlihfaidh")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();
    }
    
    @Test
    @WithMockUser
    public void canDeleteUserWhenAuthenticated() {
        Mockito.when(accountService.delete(eq("oidasajfdlihfaidh"))).thenReturn(Mono.just(account2));
         
        client
                .delete().uri("/accounts/{id}", "oidasajfdlihfaidh")
                .exchange()
                .expectStatus()
                    .isNoContent()
                .expectBody()
                    .isEmpty();
    }
    
    @Test
    public void cannotDeleteUserWhenNotAuthenticated() {
        Mockito.when(accountService.delete(eq("oidasajfdlihfaidh"))).thenReturn(Mono.just(account2));
         
        client
                .delete().uri("/accounts/{id}", "oidasajfdlihfaidh")
                .exchange()
                .expectStatus()
                    .isUnauthorized()
                .expectBody()
                    .isEmpty();
    }
}
