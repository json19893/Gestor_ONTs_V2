package totalplay.snmpv2.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;
//@EnableScheduling
@SpringBootApplication
@Slf4j
//@EnableDiscoveryClient
public class Snmpv2Application {

	public static void main(String[] args) {
		SpringApplication.run(Snmpv2Application.class, args);
	}

}
