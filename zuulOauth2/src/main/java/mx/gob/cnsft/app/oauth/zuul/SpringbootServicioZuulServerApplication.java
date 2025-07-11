package mx.gob.cnsft.app.oauth.zuul;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
public class SpringbootServicioZuulServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioZuulServerApplication.class, args);
	}
	


}
