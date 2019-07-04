package com.giocosmiano.exploration.chapter08;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class WebSocketsHystrixDashboardApplication {

    /*
     @SpringBootApplication declares this to be a Spring Boot application. We don't need, because
     we don't intend to hook into Eureka, nor institute any circuit @SpringCloudApplication breakers

     @EnableHystrixDashboard will start up a UI
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(WebSocketsHystrixDashboardApplication.class).run(args);
    }
}
