package com.giocosmiano.exploration.chapter07.comments.controller;

import com.giocosmiano.exploration.chapter07.comments.domain.Comment;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Controller
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
        this.flux = Flux.<Message<Comment>>create(emitter -> this.commentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        if (commentSink != null) {
            return newComment
                    .map(comment -> commentSink.next(MessageBuilder
                            .withPayload(comment)
                            .build()))
                    .then(Mono.just("redirect:/"));
        } else {
            return Mono.just("redirect:/");
        }
    }

    @StreamEmitter
    public void emit(@Output(Source.OUTPUT) FluxSender output) {
        output.send(this.flux);
    }
}