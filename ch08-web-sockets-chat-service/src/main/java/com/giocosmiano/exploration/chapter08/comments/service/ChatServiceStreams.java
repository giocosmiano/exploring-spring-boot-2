package com.giocosmiano.exploration.chapter08.comments.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ChatServiceStreams {

	/*
	 It's important to realize that if we receive the Flux of messages, and
	 turn around and broadcast them on the same WebSocketSession, the only person receiving the messages will
	 be the person that sent them-​ -an echo server if you will.

	 This is why we need a broker if we want to broadcast such messages. Incoming messages must be
	 received, relayed to a broker, and then picked up on the other side by all clients.

	 Now, where can we find a broker? We already have one! We've been using Spring Cloud Stream to
	 transport messages over RabbitMQ on our behalf. We can do the same for these messages as well.

	 It's important to remember that Spring Cloud Stream operates on the channel paradigm. Everything is
	 sent and received over channels. Up until now, we've gotten by using Source , Sink , and Processor , three
	 interfaces that work with output and input . To handle new comment-based messages, client-to-server user
	 messages, and server-to-client user messages, those two channels aren't enough.

	 So, we need to define a new set of streams. We can do that by creating our own interface,
	 ChatServiceStreams in the chat micro service

	 These map onto a channel name of newComments, clientToBroker, and brokerToClient (see chat-service.yaml in config-repo).

	 newComments() is defined as an input linked to the NEW_COMMENTS channel via the @Input annotation, and
	 has a return type of SubscribableChannel, meaning, it can be used to consume messages.

	 clientToBroker() is defined as an output linked to the CLIENT_TO_BROKER channel via the @Output
	 annotation, and has a return type of MessageChannel, which means that it can be used to transmit
	 messages.

	 brokerToClient() is defined as an input linked to the BROKER_TO_CLIENT channel via the @Input annotation,
	 and also has a return type of SubscribableChannel, which means it, too, can be used to consume
	 messages

	 In chat.yaml, the entry for spring.cloud.stream.bindings.clientToBroker, where clientToBroker matches the channel
	 name we set in ChatServiceStreams. It indicates that messages transmitted over the clientToBroker channel
	 will be put on RabbitMQ's learning-spring-boot-chat-user-messages exchange, and grouped with other
	 messages marked app-chatMessages
	 */
	String NEW_COMMENTS = "newComments";
	String CLIENT_TO_BROKER = "clientToBroker";
	String BROKER_TO_CLIENT = "brokerToClient";

	String USER_HEADER = "User";

	@Input(NEW_COMMENTS)
	SubscribableChannel newComments();

	@Output(CLIENT_TO_BROKER)
	MessageChannel clientToBroker();

	@Input(BROKER_TO_CLIENT)
	SubscribableChannel brokerToClient();
}
