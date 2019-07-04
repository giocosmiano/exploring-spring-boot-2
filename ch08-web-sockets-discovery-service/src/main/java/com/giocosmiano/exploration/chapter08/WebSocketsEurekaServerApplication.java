package com.giocosmiano.exploration.chapter08;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WebSocketsEurekaServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WebSocketsEurekaServerApplication.class).run(args);
    }
}
