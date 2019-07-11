package com.giocosmiano.exploration.chapter08.comments.service;

import reactor.core.publisher.Mono;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

@Service
@EnableBinding(ChatServiceStreams.class)
public class InboundChatService extends UserParsingHandshakeHandler {

	private final ChatServiceStreams chatServiceStreams;

	public InboundChatService(ChatServiceStreams chatServiceStreams){
		this.chatServiceStreams = chatServiceStreams;
	}

	/*
	 @EnableBinding(ChatServiceStreams.class) signals Spring Cloud Stream to connect this component to its
	 broker-handling machinery.

	 It implements the WebSocketHandler interface--when a client connects, the handle(WebSocketSession)
	 method will be invoked.

	 Instead of using the @StreamListener annotation as in the previous samples, this class injects a
	 ChatServiceStreams bean (same as the binding annotation) via constructor injection.

	 To handle a new WebSocketSession, we grab it and invoke its receive() method. This hands us a Flux of
	 potentially endless WebSocketMessage objects. These would be the incoming messages sent in by the
	 client that just connected. NOTE: Every client that connects will invoke this method
	 independently.

	 We map the Flux<WebSocketMessage> object's stream of payload data into a Flux<String> via
	 getPayloadAsText().

	 From there, we transform each raw message into a formatted message with the WebSocket's
	 session ID prefixing each message.

	 Satisfied with our formatting of the message, we flatMap it onto our broadcast() message in order to
	 broadcast it to RabbitMQ.

	 To hand control to the framework, we put a then() on the tail of this Reactor flow so Spring can
	 subscribe to this Flux.

	 The broadcast method, invoked as every message is pulled down, marshals and transmits the
	 message by first building a Spring Cloud Streams Message<String> object. It is pushed out over the
	 ChatServiceStreams.clientToBroker() object's MessageChannel via the send() API. To reactor-ize it, we wrap
	 it with Mono.fromRunnable

	 Such is the effect of functional reactive programming (FRP). Not a lot of
	 effort is spent on imperative constructs and intermediate results. Instead, each step is chained to the next
	 step, forming a transforming flow, pulling data from one input (the WebSocketSession in this case), and
	 steering it into a channel for the broker ( ChatServiceStreams.clientToBroker() )

	 This InboundChatService routes individual messages from client to server to broker, we are able to take
	 individual messages and broadcast them to ALL users. Then, with OutboundChatService pulling down
	 copies of the message for each WebSocket session, each user is able to receive a copy

	 The names InboundChatService and OutboundChatService are somewhat arbitrary. The important point to note is
	 that one is responsible for transporting WebSocket messages from the client to the broker through the
	 server. Those are incoming. After crossing the broker, we describe them at this stage as being outgoing.
	 The naming convention is meant to help remember what does what. Neither Spring Boot nor Spring
	 Cloud Stream care about what these classes are named.
	 */
	@Override
	protected Mono<Void> handleInternal(WebSocketSession session) {
		return session
			.receive()
			.log(getUser(session.getId())
				+ "-inbound-incoming-chat-message")
			.map(WebSocketMessage::getPayloadAsText)
			.log(getUser(session.getId())
				+ "-inbound-convert-to-text")
			.flatMap(message ->
				broadcast(message, getUser(session.getId())))
			.log(getUser(session.getId())
				+ "-inbound-broadcast-to-broker")
			.then();
	}

	public Mono<?> broadcast(String message, String user) {
		return Mono.fromRunnable(() -> {
			chatServiceStreams.clientToBroker().send(
				MessageBuilder
					.withPayload(message)
					.setHeader(ChatServiceStreams.USER_HEADER, user)
					.build());
		});
	}

}
