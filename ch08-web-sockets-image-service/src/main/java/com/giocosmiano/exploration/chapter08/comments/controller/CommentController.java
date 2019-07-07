package com.giocosmiano.exploration.chapter08.comments.controller;

import com.giocosmiano.exploration.chapter08.comments.domain.Comment;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@EnableBinding(Source.class)
public class CommentController {
    private final MeterRegistry meterRegistry;
    private FluxSink<Message<Comment>> commentSink;
    private Flux<Message<Comment>> flux;

    /*
     @EnableBinding(Source.class) flags this app as a source for new events. Spring Cloud Stream uses this
     annotation to signal the creation of channels, which, in RabbitMQ, translates to exchanges and
     queues

     The constructor proceeds to set up a FluxSink, the mechanism to emit new messages into a
     downstream Flux. This sink is configured to ignore downstream back-pressure events. It starts
     publishing right away, auto-connecting to its upstream source upon subscription

     The objects being emitted are Message<Comment>, which is Spring's abstraction for a POJO wrapped as
     a transportable message. This includes the ability to add headers and other information

     Inside addComments, if the sink has been established, it maps newComment into a Message<Comment> using
     Spring Messaging APIs. Finally, it transmits the message into the sink

     When the message is successfully emitted to Flux, a redirect is issued

     To transmit Flux of Message<Comment> objects, a separate method, emit , is wired up with an @StreamEmitter
     annotation. This method is fed a FluxSender, which provides us with a Reactor-friendly means to
     transmit messages into a channel. It lets us hook up the Flux tied to our FluxSink

     @Output(Source.OUTPUT) annotation marks up which channel it gets piped to (visiting Source.OUTPUT
     reveals the channel name as output)

     First of all, it's not common practice to create a Flux and then add to it. The paradigm is to wrap it
     around something else. To drive this point home, Flux itself is an abstract class. You can't instantiate it.
     Instead, you must use its various static helper methods to craft one. So, when we want to take a
     behavior that is tied to users clicking on a site and link it to a Flux that was created when the application
     started, we need something like FluxSink to bridge these two things together

     Spring Cloud Stream focuses on chaining together streams of messages with source/sink semantics

     When it comes to Reactor, this means adapting a Flux of messages onto a channel, a concept curated for
     several years by Spring Integration. Given that the concrete nature of the channel is abstracted away, it
     doesn't matter what transport technology we use. Thanks to the power of Spring Boot, this is defined by
     dependencies on the classpath. Nevertheless, we'll continue using RabbitMQ because it's darn simple
     and powerful at the same time
     */
    public CommentController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.flux = Flux.<Message<Comment>>
                create(emitter -> this.commentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    /*
     redirect:/ is a Spring Web signal to re-render the page at / via an HTTP redirect. Since we are shifting
     into dynamically updating the page based on asynchronous WebSocket messages, this is no longer the
     best way.

     What are the issues? A few can be listed as follows:

     If the comment hasn't been saved (yet), the redirect would re-render the page with no change at all.

     The redirect may cause an update in the midst of handling the new comment's WebSocket message.
     Based on the race conditions, the comment may not yet be saved, causing it to not appear, and the
     refresh may miss the asynchronous message, causing the entire comment to not be displayed unless
     the page is manually refreshed.

     Setting up a WebSocket handler with every new comment isn't efficient.

     Either way, this isn't a good use of resources, and could introduce timing issues. Instead, it's best if we
     convert this into an AJAX call

     To support the fact that we are now making an AJAX call, and not expecting a redirect, we need to
     make alterations on the server side.
     For one thing, we need to change the image micro service's CommentController from being view-based to
     being a REST controller.

     @Controller marked it as a Spring WebFlux controller that was expected to return the HTTP redirect

     By replacing @Controller with @RestController , we have marked this class as a Spring WebFlux controller
     with results written directly into the HTTP response body

     The return type has switched from Mono<String> to Mono<ResponseEntity<?>> . ResponseEntity<?> is a Spring
     Web container that holds HTTP response headers, body, and status code.

     The logic for forwarding messages to the comments service over a FluxSink to Spring Cloud Stream is the same

     The last line of both the if and the else clauses uses the static builder methods of ResponseEntity to
     generate an HTTP 204 (No Content) response. It indicates success, but no response body is included.
     Considering the client isn't interested in any content, that's good enough
     */
    @PostMapping("/comments")
    public Mono<ResponseEntity<?>> addComment(Mono<Comment> newComment) {
        if (commentSink != null) {
            return newComment
                    .map(comment -> {
                        commentSink.next(MessageBuilder
                                .withPayload(comment)
                                .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .build());
                        return comment;
                    })
                    .flatMap(comment -> {
                        meterRegistry
                                .counter("comments.produced", "imageId", comment.getImageId())
                                .increment();
//                        return Mono.just("redirect:/");
                        return Mono.just(ResponseEntity.noContent().build());
                    });
        } else {
//            return Mono.just("redirect:/");
            return Mono.just(ResponseEntity.noContent().build());
        }
    }

    @StreamEmitter
    @Output(Source.OUTPUT)
    public void emit(FluxSender output) {
        output.send(this.flux);
    }
}