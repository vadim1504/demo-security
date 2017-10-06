package by.vadim.demosecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class WebConfiguration {

    private Mono<ServerResponse> message(ServerRequest serverRequest){
        Mono<String> principalPublisher = serverRequest.principal().map(p->"Hello "+p.getName()+"!");
        return ok().body(principalPublisher,String.class);
    }

    private Mono<ServerResponse> username(ServerRequest serverRequest){
        Mono<UserDetails> detailsMono= serverRequest.principal().map(p-> UserDetails.class.cast(Authentication.class.cast(p).getPrincipal()));
        return ok().body(detailsMono,UserDetails.class);
    }

    @Bean
    RouterFunction<?> routes(){
        return route(GET("/message"),this::message).andRoute(GET("/users/{username}"),this::username);
    }
}
