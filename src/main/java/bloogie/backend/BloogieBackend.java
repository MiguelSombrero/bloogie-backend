package bloogie.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 *
 * @author miika
 */

@EnableWebFlux
public class BloogieBackend {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(BloogieBackend.class);
    }
}
