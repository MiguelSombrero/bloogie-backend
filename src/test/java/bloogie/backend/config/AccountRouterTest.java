
package bloogie.backend.config;

import bloogie.backend.domain.Account;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRouterTest {
    
    private WebTestClient client;

    @Autowired
    private ReactiveMongoTemplate template;
    
    @Autowired
    private ApplicationContext context;

    @Before
    public void setUp() {
        client = WebTestClient
                .bindToApplicationContext(context)
                .build();
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
