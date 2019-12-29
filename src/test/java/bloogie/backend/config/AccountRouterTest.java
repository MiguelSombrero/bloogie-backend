
package bloogie.backend.config;

import bloogie.backend.BloogieBackend;
import bloogie.backend.domain.Account;
import bloogie.backend.domain.Blog;
import com.mongodb.reactivestreams.client.MongoClient;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
public class AccountRouterTest {
    
    private WebTestClient client;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setUp() {
        client = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .build();
    }
    
    @Test
    public void canAddUserToDatabase() {
        Account account = new Account();
        account.setId("1");
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
                .expectBodyList(Account.class)//.hasSize(0)
                .returnResult();
        
    }
    
}
