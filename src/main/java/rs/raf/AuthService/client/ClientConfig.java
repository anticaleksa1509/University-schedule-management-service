package rs.raf.AuthService.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Logger;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate authServiceRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8081/api/nastavnik"));
        restTemplate.setInterceptors(Collections.singletonList(new TokenInterceptor()));
        return restTemplate;
    }

    private class TokenInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                            ClientHttpRequestExecution clientHttpRequestExecution)
                throws IOException {
            //pristupamo telu http zahteva i nakon toga mozemo da uzmemo authorization heder i da nastavimo logiku
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                String token = authorizationHeader.substring(7).trim();
                //trim -  koristimo kako bi se uklonili eventualni razmaci na pocetku ili kraju stringa
               httpRequest.getHeaders().add("Authorization", "Bearer " + token);
               //ovom linijom mi ustvari dodajemo autorization heder sa tokenom u zahtev koji gadjamo
                //i time mozemo da proveramo autorizaciju na drugom servisu

            }

            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }
    }
}
