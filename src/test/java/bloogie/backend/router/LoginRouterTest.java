
package bloogie.backend.router;

import bloogie.backend.domain.Account;
import bloogie.backend.service.AccountService;
import bloogie.backend.service.CustomReactiveUserDetailsService;
import bloogie.backend.utils.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@AutoConfigureWebTestClient
@SpringBootTest
public class LoginRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private TestUtils utils;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @MockBean
    private AccountService accountService;
    
    //@MockBean
    //private CustomReactiveUserDetailsService userDetailsService;
    
    private Account account;
    
    @BeforeEach
    public void setUp() {
        this.account = utils.giveUser("oidasajfdlihfaidh", "Jukka Riekkonen", "jukka", "jukka");
    }
    
    @Test
    public void loginFailsWithBadCredentials() {
        Mockito.when(accountService.getAuthenticatedUser()).thenReturn(Mono.just(account));
                
        client
                .post().uri("/login")
                .header("Authorization", "Basic " + utils.createAuthenticationToken("justiina", "juuso"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty();
    }
    
    // not yet working if database doesn't include "jukka" - how to mock?
    
    @Test
    public void loginSuccessWithGoodCredentials() {
        Mockito.when(accountService.getAuthenticatedUser()).thenReturn(Mono.just(account));
        //Mockito.when(userDetailsService.findByUsername(eq("jukka"))).thenReturn(Mono.just(account.toUserDetails()));
        
        client
                .post().uri("/login")
                .header("Authorization", "Basic " + utils.createAuthenticationToken("jukka", "jukka"))
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
}
