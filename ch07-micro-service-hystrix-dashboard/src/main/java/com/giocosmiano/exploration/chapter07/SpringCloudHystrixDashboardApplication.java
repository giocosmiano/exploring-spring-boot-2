package com.giocosmiano.exploration.chapter07;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class SpringCloudHystrixDashboardApplication {

    /*
     @SpringBootApplication declares this to be a Spring Boot application. We don't need, because
     we don't intend to hook into Eureka, nor institute any circuit @SpringCloudApplication breakers

     @EnableHystrixDashboard will start up a UI
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringCloudHystrixDashboardApplication.class).run(args);
    }
}
