package com.giocosmiano.exploration.chapter08.comments.service;

import java.util.HashMap;
import java.util.Map;

import com.giocosmiano.exploration.chapter08.comments.service.CommentService;
import com.giocosmiano.exploration.chapter08.comments.service.InboundChatService;
import com.giocosmiano.exploration.chapter08.comments.service.OutboundChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
public class WebSocketConfig {

	/*
	 HandlerMapping bean is a Spring's interface for linking routes with handler methods.

	 The name of the method, webSocketMapping, indicates this method is about wiring routes for
	 WebSocket message handling.

	 It asks for a copy of the CommentService bean we defined earlier. Since Spring Boot activates
	 component scanning, an instance of that service will be created automatically, thanks to the @Service
	 annotation we put on it earlier.

	 We create a Java Map, designed for mapping string-based routes onto WebSocketHandler objects, and
	 dub it a urlMap .

	 We load the map with /topic/comments.new , and link it with our CommentService , a class that implements
	 the WebSocketHandler interface.

	 There's the sticky issue of micro services, whereby, our chat service runs on a different port from the
	 frontend image service. Any modern web browser will deny a web page calling a different port from
	 the original port it was served. To satisfy security restrictions (for now), we must implement a
	 @Bean custom Cross-origin Resource Sharing or CORS policy. In this case, we add an Allowed Origin
	 of http://localhost:8080, the address where the frontend image service resides.

	 With both the urlMap and the corsConfiguration policy, we construct SimpleUrlHandlerMapping. It also needs
	 an order level of 10 to get viewed ahead of certain other route handlers provided automatically by
	 Spring Boot

	 Essentially, this bean is responsible for mapping WebSocket routes to handlers, whether that is to target
	 client-to-server, or server-to-client messaging. The message route we've designed so far is a WebSocket
	 message that originates on the server when a new comment is created, and is pushed out to all clients so
	 they can be alerted to the new comment.

	 In Spring Framework 4, there is an annotation-based mechanism that lets us configure these routes
	 directly on the handlers themselves. But for Spring Framework 5 (WebFlux), we must configure things
	 by hand. CORS is also critical to handle given the way we split things up across multiple micro services
	 */
	@Bean
	HandlerMapping webSocketMapping(CommentService commentService,
									InboundChatService inboundChatService,
									OutboundChatService outboundChatService) {
		Map<String, WebSocketHandler> urlMap = new HashMap<>();
		urlMap.put("/topic/comments.new", commentService);
		urlMap.put("/app/chatMessage.new", inboundChatService);
		urlMap.put("/topic/chatMessage.new", outboundChatService);

		Map<String, CorsConfiguration> corsConfigurationMap =
			new HashMap<>();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("http://localhost:9072"); // port of front-end Image micro service
		corsConfigurationMap.put(
			"/topic/comments.new", corsConfiguration);
		corsConfigurationMap.put(
			"/app/chatMessage.new", corsConfiguration);
		corsConfigurationMap.put(
			"/topic/chatMessage.new", corsConfiguration);

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(10);
		mapping.setUrlMap(urlMap);
		mapping.setCorsConfigurations(corsConfigurationMap);

		return mapping;
	}

	@Bean
	WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
