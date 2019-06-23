package com.giocosmiano.exploration.chapter07;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudEurekaServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringCloudEurekaServerApplication.class).run(args);
    }
}
