
package bloogie.backend.config;

import bloogie.backend.domain.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author miika
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRouterTest {
    
    @Autowired
    private WebTestClient client;

    @Autowired
    private TestUtils utils;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Before
    public void setUp() {
        utils.saveUser("Jukka Roinanen", "jukka", encoder.encode("jukka"));
    }
    
    @Test
    public void canGetUsersFromDtabase() {
        Mono<Long> users = utils.getUsers().count();
        
        client
                .get().uri("/account")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(Account.class).hasSize(1);
    }
    
}
