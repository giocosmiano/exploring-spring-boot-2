package com.giocosmiano.exploration.chapter08.comments.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

@Service
@EnableBinding(ChatServiceStreams.class)
public class OutboundChatService
				extends UserParsingHandshakeHandler {

	private final static Logger log =
		LoggerFactory.getLogger(CommentService.class);

	private Flux<Message<String>> flux;
	private FluxSink<Message<String>> chatMessageSink;

	public OutboundChatService() {
		this.flux = Flux.<Message<String>>create(
			emitter -> this.chatMessageSink = emitter,
			FluxSink.OverflowStrategy.IGNORE)
			.publish()
			.autoConnect();
	}

	/*
	 It has the same EnableBinding(ChatServicesStreams.class) as the inbound service, indicating that this, too,
	 will participate with Spring Cloud Streams.

	 The constructor call wires up another one of those FluxSink objects, this time for a Flux or strings.

	 @StreamListener(ChatServiceStreams.BROKER_TO_CLIENT) indicates that this service will be listening for
	 incoming messages on the brokerToClient channel. When it receives one, it will forward it to
	 chatMessageSink.

	 This class also implements WebSocketHandler , and each client attaches via the handle(WebSocketSession)
	 method. It is there that we connect the flux of incoming messages to the WebSocketSession via its send()
	 method.

	 Because WebSocketSession.send() requires Flux<WebSocketMessage> , we map the Flux<String> into it using
	 session::textMessage . Nothing to serialize.

	 There is a custom log flag when the Flux finished, and another for when the entire Flux is handled

	 Just like InboundChatService, we implement handleInternal(WebSocketSession) .

	 It has the same session.send(Flux) call, but that Flux has a couple of extra steps added, including a
	 filter and an extra map.

	 The filter call validates each message, deciding whether or not this user should get it. (We'll write
	 that validate() method in a moment).

	 Assuming the message is valid for this user, it uses a local transform method to tweak it.

	 The rest of the machinery used to convert this string message into a WebSocketMessage<String> and pipe
	 it over the WebSocket is the same as before
	 */
	@StreamListener(ChatServiceStreams.BROKER_TO_CLIENT)
	public void listen(Message<String> message) {
		if (chatMessageSink != null) {
			log.info("Publishing " + message +
				" to websocket...");
			chatMessageSink.next(message);
		}
	}

	@Override
	protected Mono<Void> handleInternal(WebSocketSession session) {
		return session
			.send(this.flux
				.filter(s -> validate(s, getUser(session.getId())))
				.map(this::transform)
				.map(session::textMessage)
				.log(getUser(session.getId()) +
					"-outbound-wrap-as-websocket-message"))
			.log(getUser(session.getId()) +
				"-outbound-publish-to-websocket");
	}

	/*
	 validate accepts a Message<String> and the name of the current user (not the user that sent the
	 message).

	 It first checks the payload, and if it starts with @ , it looks deeper. If the message does NOT start
	 with @ , it just lets it on through.

	 If the message starts with @ , it proceeds to extract the target user by parsing the text between @ and
	 the first space. It also extracts the original sender of the message using the User header.

	 If the current user is either the sender or the receiver, the message is allowed through. Otherwise, it
	 is dropped

	 A filtering function like this makes it easy to layer various options. We used it to target user-specific
	 messages. But imagine putting things like security checks, regional messages, time-based messages, and more
	 */
	private boolean validate(Message<String> message, String user) {
		if (message.getPayload().startsWith("@")) {
			String targetUser = message.getPayload()
				.substring(1, message.getPayload().indexOf(" "));

			String sender = message.getHeaders()
				.get(ChatServiceStreams.USER_HEADER, String.class);
			
			return user.equals(sender) || user.equals(targetUser);
		} else {
			return true;
		}
	}

	/*
	 accepts a Message<String> , and converts it into a plain old string message

	 It extracts the User header to find who wrote the message

	 If the message starts with @ , then it assumes the message is targeted, and prefixes it with the author
	 wrapped in parentheses

	 If the message does NOT start with @ , then it prefixes it with the author wrapped in parentheses
	 plus (all), to make it clear that this is a broadcast message
	 transform

	 With this change in place, we have coded a sophisticated user-to-user chat service, running on top of
	 RabbitMQ, using Reactive Streams
	 */
	private String transform(Message<String> message) {
		String user = message.getHeaders()
			.get(ChatServiceStreams.USER_HEADER, String.class);
		if (message.getPayload().startsWith("@")) {
			return "(" + user + "): " + message.getPayload();
		} else {
			return "(" + user + ")(all): " + message.getPayload();
		}
	}
}
