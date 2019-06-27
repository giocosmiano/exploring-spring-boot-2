package com.giocosmiano.exploration.chapter07;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigApplication {

    /*
     dependencies {
        compile 'org.springframework.cloud:spring-cloud-config-server'
     }

     dependencyManagement {
        imports {
            mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
        }
     }

     spring-cloud-config-server is only needed to run a config server, not a config server client

     @SpringBootApplication marks this as a Spring Boot application. Since this is the cornerstone of the
     rest of our micro services (including Eureka), it doesn't use Eureka

     @EnableConfigServer launches an embedded Spring Cloud Config Server, full of options. We'll use the
     defaults as much as possible
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringCloudConfigApplication.class).run(args);
    }
}
