package com.giocosmiano.exploration.chapter08.comments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giocosmiano.exploration.chapter08.comments.domain.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

//@Service
//@EnableBinding(Sink.class)
public class WebSocketHandlerCommentService implements WebSocketHandler {

    private final static Logger log =
            LoggerFactory.getLogger(CommentService.class);

    private ObjectMapper mapper;
    private Flux<Comment> flux;
    private FluxSink<Comment> webSocketCommentSink;

    /*
     @Service marks this as a Spring bean, picked up automatically when the chat micro service starts

     @EnableBinding(Sink.class) shows this to be a receiver for Spring Cloud Stream messages

     This service implements WebSocketHandler, a WebFlux interface that comes with a
     handle(WebSocketSession) method

     This service needs to consume the messages sent from Spring Cloud Stream. However, the destination
     for these messages is not another Spring Cloud Stream destination. Instead, we want to pipe them into a
     WebSocket session. In essence, we need to pull messages down from a RabbitMQ-based Flux, and forward them to a Flux
     connected to a WebSocket session. This is where we need another one of those FluxSink objects

     In addition, we need a Jackson ObjectMapper, and will get it from Spring's container through constructor
     injection.

     To create a FluxSink that lets us put comments one by one onto a Flux , we use Flux.create() , and let it
     initialize our sink, webSocketCommentSink .

     When it comes to back-pressure policy, it's wired to ignore back-pressure signals for simplicity's
     sake. There may be other scenarios where we would select differently.

     publish() and autoConnect() kick our Flux into action so that it's ready to start transmitting once hooked
     into the WebSocket session

     The idea we are shooting for is to put events directly onto webSocketCommentSink, and then hitch the
     corresponding flux into the WebSocket API. Think of it like webSocketCommentSink as the object we can
     append comments to, and flux being the consumer pulling them off on the other end (after the consumer
     subscribes)
     */
    WebSocketHandlerCommentService(ObjectMapper mapper) {
        this.mapper = mapper;
        this.flux = Flux.<Comment>create(
                emitter -> this.webSocketCommentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    /*
     The broadcast() method is marked as a @StreamListener for Sink.INPUT. Messages get de-serialized as
     Comment objects thanks to the application/json setting.

     The code checks if our webSocketCommentSink is null, indicating whether or not it's been created.

     A log message is printed.

     The Comment is dropped into our webSocketSink, which means that it will become available to our
     corresponding flux automatically
     */
    @StreamListener(Sink.INPUT)
    public void broadcast(Comment comment) {
        if (webSocketCommentSink != null) {
            log.info("Publishing " + comment.toString() +
                    " to websocket...");
            webSocketCommentSink.next(comment);
        }
    }

    /*
     We handed a WebSocketSession

     The Comment-based Flux is piped into the WebSocket via its send() method

     This Flux itself is transformed from a series of Comment objects into a series of JSON objects courtesy
     of Jackson, and then, finally, into a series of WebSocketMessage objects

     It's important to point out that in Spring Framework 4, much of this was handled by the inner working
     of Spring's WebSocket API as well as its Messaging API. There was no need to serialize and deserialize
     Java POJOs into JSON representations. That was provided out of the box by Spring's converter services.

     In Spring Framework 5, in the WebFlux module, the WebSocket API is very simple. Think of it as
     streams of messages coming and going. So, the duty of transforming a chain of Comment objects into one
     of JSON-encoded text messages is paramount. As we've just seen, with the functional paradigm of
     Reactor, this is no bother
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(this.flux
                .map(comment -> {
                    try {
                        return mapper.writeValueAsString(comment);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .log("encode-as-json")
                .map(session::textMessage)
                .log("wrap-as-websocket-message"))
                .log("publish-to-websocket");
    }
}
