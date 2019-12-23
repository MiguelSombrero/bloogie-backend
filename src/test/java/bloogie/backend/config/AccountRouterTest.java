
package bloogie.backend.config;

import bloogie.backend.domain.Account;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private ReactiveMongoTemplate template;
    
    @BeforeEach
    public void setUp(ApplicationContext context) {  
        client = WebTestClient.bindToApplicationContext(context).build();
    }
    
    @Test
    public void canAddUserToDatabase() {
        Account account = new Account();
        account.setUsername("miika");
        account.setPassword("miika");
        account.setName("Miika");
        
        client
                .post().uri("/accounts")
                .body(Mono.just(account), Account.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();   
    }
    
    @Test
    public void canGetUsersFromDtabase() {
        EntityExchangeResult<List<Account>> users = client
                .get().uri("/accounts")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(Account.class).hasSize(0)
                .returnResult();
        
    }
    
}
