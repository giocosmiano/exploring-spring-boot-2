package com.giocosmiano.exploration.chapter06.comments.controller;

import com.giocosmiano.exploration.chapter06.comments.domain.Comment;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {

    private final RabbitTemplate rabbitTemplate;
    private final MeterRegistry meterRegistry;

    public CommentController(RabbitTemplate rabbitTemplate,
                             MeterRegistry meterRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;
    }

    /*
     RabbitTemplate initialized by constructor injection. This RabbitTemplate is created
     automatically by Spring Boot when it spots spring-amqp on the classpath

     @PostMapping("/comments") annotation registers this method to respond to the form submissions
     that we added earlier in the template with th:action="@{'/comments'}"

     Spring will automatically convert the body of the POST into a Comment domain object. Additionally,
     since we are using WebFlux, deserializing the request body is wrapped in a Mono, hence that process
     will only occur once the framework subscribes to the flow

     The incoming Mono<Comment> is unpacked using flatMap and then turned into a
     rabbitTemplate.convertAndSend() operation, which itself is wrapped in Mono.fromRunnable

     The comment is published to RabbitMQ's learning-spring-boot exchange with a routing key of
     comments.new

     We wait for this to complete with then(), and when done, return a Spring WebFlux redirect to send
     the web page back to the home page

     The comment is published to RabbitMQ's `learning-spring-boot` exchange with a routing key
     of `comments.new`

     Another factor is that RabbitMQ is not reactive. Invoking rabbitTemplate.convertAndSend() is blocking. That
     may sound awkward given AMQP is a pub/sub technology. But the whole process of publishing the
     message to the RabbitMQ broker holds up our thread, and is, by definition, blocking

     So the code wraps that inside a Java Runnable and converts it into a Mono via Reactor's Mono.fromRunnable.
     That makes it possible to invoke this blocking task only when we're ready at the right time. It's
     important to know that a Mono-wrapped-Runnable doesn't act like a traditional Java Runnable and doesn't
     get launched in a separate thread. Instead, the Runnable interface provides a convenient wrapper where
     Reactor controls precisely when the run() method is invoked inside its scheduler

     MeterRegistry is used to increment a comments.produced metric with every comment

     Each metric is also "tagged" with the related imageId

     We have to tune the Mono wrapping our rabbitTemplate.convertAndSend() , and ensure that the comment is
     passed via then(). Then it must be unpacked via flatMap in the part of the flow that writes metrics

     Should the code talking to the meterRegistry also be wrapped in Mono.fromRunnable()? Perhaps.
     The code blocks when writing, but in this incarnation, the metrics are stored in memory, so
     the cost is low. Nevertheless, the cost could rise, meaning it should be properly managed.
     If the service became external, the odds would increase quickly in favor of wrapping with
     a separate Mono
     */

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.flatMap(comment ->
                Mono.fromRunnable(() ->
                        rabbitTemplate
                                .convertAndSend(
                                        "learning-spring-boot", // exchange
                                        "comments.new", // routingKey
                                        comment))
                        .then(Mono.just(comment)))
                .log("commentService-publish")
                .flatMap(comment -> {
                    meterRegistry
                            .counter("comments.produced", "imageId", comment.getImageId())
                            .increment();
                    return Mono.just("redirect:/");
                });
    }
}