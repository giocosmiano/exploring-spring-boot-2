package com.giocosmiano.exploration.chapter08;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class WebSocketsCommentApplication {

    /*
     @SpringCloudApplication replaces the previous @SpringBootApplication . This new annotation extends
     @SpringBootApplication , giving us the same auto-configuration, component scanning, and property
     support (among other things) that we have come to love. Additionally, it adds @EnableDiscoveryClient
     to register with Eureka and @EnableCircuitBreaker so we can create fallback commands if a remote
     service is down

     There are both @EnableEurekaClient and @EnableDiscoveryClient annotations available.
     DiscoveryClient is the abstract interface that Spring Cloud Netflix puts above EurekaClient in
     the event that future service registry tools are built. At this point in time, there is little
     difference in our code, except the convenient usage of a single
     annotation, @SpringCloudApplication, to turn our component into a micro service

     Since this micro-service has @EnableCircuitBreaker (pulled in via @SpringCloudApplication), the
     /hystrix.stream endpoint can be entered in the Hystrix Dashboard outputting circuit metrics
     */
    public static void main(String[] args) {
        SpringApplication.run(
                WebSocketsCommentApplication.class);
    }
}
