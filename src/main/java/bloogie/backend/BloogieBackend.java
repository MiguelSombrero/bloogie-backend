package bloogie.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 *
 * @author miika
 */


@EnableReactiveMongoRepositories
@SpringBootApplication
public class BloogieBackend {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(BloogieBackend.class);
        
    }
}
