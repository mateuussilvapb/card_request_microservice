package io.github.mateuussilvapb.msCartoes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@Slf4j
@EnableRabbit
@EnableEurekaClient
@SpringBootApplication
public class MsCartoesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCartoesApplication.class, args);
    }

}
