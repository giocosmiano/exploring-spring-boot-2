package com.giocosmiano.exploration.chapter08.comments.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

abstract class UserParsingHandshakeHandler
				implements WebSocketHandler {
	
	private final Map<String, String> userMap;

	UserParsingHandshakeHandler() {
		this.userMap = new HashMap<>();
	}

	/*
	 This abstract class implements WebSocketHandler ; it will be invoked when a new WebSocketSession is
	 created

	 It contains a mapping between session ID and username, called userMap , initialized in the
	 constructor

	 The implementation of handle(WebSocketSession) takes the userMap and puts a new entry keyed off the
	 session's ID

	 The value stored under that session ID is extracted from the session's handshake, granting access to
	 the original URI

	 With some Java 8 stream magic, we can extract the query string from this URI, and find the user
	 argument

	 findFirst() produces an Optional , so we can either map over the answer or fall back to an empty
	 string (no user)

	 Having loaded the userMap, we then invoke the concrete subclass through a custom abstract method,
	 handleInternal(WebSocketMessage)

	 To facilitate looking up the current username, getUser(String) is provided to look up user based on session ID

	 This chunk of code will handle user details, allowing each concrete WebSocketHandler to do its thing while
	 also having access to the current session's username
	 */
	@Override
	public final Mono<Void> handle(WebSocketSession session) {

		this.userMap.put(session.getId(),
				Stream.of(session.getHandshakeInfo().getUri()
							.getQuery().split("&"))
					.map(s -> s.split("="))
					.filter(strings -> strings[0].equals("user"))
					.findFirst()
					.map(strings -> strings[1])
					.orElse(""));

		return handleInternal(session);
	}

	abstract protected Mono<Void> handleInternal(
							WebSocketSession session);

	String getUser(String id) {
		return userMap.get(id);
	}
}
