package com.giocosmiano.exploration.chapter07.comments.helper;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

	/*
	 There is a big limitation in this code when dealing with microservices--the URL of our target service
	 can change.

	 Getting locked into a fixed location is never good. What if the comments service changes ports? What if
	 we need to scale up multiple copies in the future?

	 The solution? We should tie in with Netflix's Ribbon service, a software load balancer that also
	 integrates with Eureka. To do so, we only need some small additions to our images service

	 First, let's create a RestTemplate object then mark this `class` as @Configuration

	 @Configuration marks this as a configuration class containing bean definitions. Since it's located
	 underneath SpringCloudImageApplication, it will be automatically picked up by component
	 scanning

	 @Bean marks the restTemplate() method as a bean definition

	 restTemplate() method returns a plain old Spring RestTemplate instance

	 @LoadBalanced instructs Netflix Ribbon to wrap this RestTemplate bean with load balancing advice
	 */
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
