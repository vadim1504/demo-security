package by.vadim.demosecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.HttpSecurity;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Bean
    UserDetailsRepository userDetailsRepository(){
	    //username нижний регист
		UserDetails vadim = User.withUsername("vadim").roles("USER","ADMIN").password("password").build();
		return new MapUserDetailsRepository(vadim);
	}

	@Bean
    SecurityWebFilterChain securityWebFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .authorizeExchange()
                .pathMatchers("/users/{username}")
                .access((mono, context) -> mono
                                .map(auth -> auth.getName().equals(context.getVariables().get("username")))
                                .map(AuthorizationDecision::new))
                .anyExchange()
                .authenticated()
                .and()
                .build();
    }

}
