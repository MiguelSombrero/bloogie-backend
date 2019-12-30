
package bloogie.backend.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Class for configuring reactive mongo client and reactive mongo template. Database name
 * is fetched from application-{profile}.properties file, with {profile} matching one in @Profile
 * annotation.
 * 
 * @author miika
 */

@Profile("default")
@Configuration
@EnableReactiveMongoRepositories
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {
 
    @Value("${spring.data.mongodb.database}")
    private String MONGO_DB;
    
    @Autowired
    private MongoClient reactiveMongoClient;
    
    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }
    
    @Bean
    @Override
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient, MONGO_DB);
    }
 
    @Override
    protected String getDatabaseName() {
        return MONGO_DB;
    }
}