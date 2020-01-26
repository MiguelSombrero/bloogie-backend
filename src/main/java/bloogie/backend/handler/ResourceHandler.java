
package bloogie.backend.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Interface that defines handler functions for domain objects (resources).
 * 
 * @author miika
 */
public interface ResourceHandler {
    Mono<ServerResponse> list(ServerRequest request);
    Mono<ServerResponse> create(ServerRequest request);
    Mono<ServerResponse> getOne(ServerRequest request);
    Mono<ServerResponse> update(ServerRequest request);
    Mono<ServerResponse> delete(ServerRequest request);
}
