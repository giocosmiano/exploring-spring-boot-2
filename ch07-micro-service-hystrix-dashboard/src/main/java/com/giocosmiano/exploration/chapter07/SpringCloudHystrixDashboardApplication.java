package com.giocosmiano.exploration.chapter07;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class SpringCloudHystrixDashboardApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringCloudHystrixDashboardApplication.class).run(args);
    }
}
