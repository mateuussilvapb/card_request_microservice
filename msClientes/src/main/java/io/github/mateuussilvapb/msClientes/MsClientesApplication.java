package io.github.mateuussilvapb.msClientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsClientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsClientesApplication.class, args);
    }

}
