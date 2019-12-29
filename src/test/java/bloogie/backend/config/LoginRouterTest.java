
package bloogie.backend.config;

import bloogie.backend.domain.Account;
import com.mongodb.reactivestreams.client.MongoClient;
import java.util.Base64;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
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
@SpringBootTest
public class LoginRouterTest {
    
    @Autowired
    MongoClient reactiveMongoClient;
    
    @Autowired
    private ApplicationContext context;
    
    private WebTestClient client;

    private String createAuthenticationToken(String username, String password) {
        String passphrase = username + ":" + password;
        return Base64.getEncoder().encodeToString(passphrase.getBytes());
    }
    
    @Before
    public void setUp() {  
        client = WebTestClient.bindToApplicationContext(context).build();
        reactiveMongoClient.getDatabase("bloogietestdb").drop();
    }
    
    @Test
    public void loginFailsWithBadCredentials() {
        client.post().uri("/login")
                .header("Authorization", "Basic " + createAuthenticationToken("justiina", "juuso"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty();
    }
    
    @Test
    public void loginSuccessWithGoodCredentials() {
        Account account = new Account();
        account.setUsername("miika");
        account.setPassword("miika");
        account.setName("Miika Juhani");
        
        client
                .post().uri("/accounts")
                .body(Mono.just(account), Account.class)
                .exchange();   
        
        EntityExchangeResult<Account> user = client
                .post().uri("/login")
                .header("Authorization", "Basic " + createAuthenticationToken("miika", "miika"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(Account.class)
                .returnResult();
        
        assertEquals("miika", user.getResponseBody().getUsername());
        assertEquals("Miika Juhani", user.getResponseBody().getName());
    }

}
