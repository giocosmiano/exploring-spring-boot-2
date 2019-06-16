package com.giocosmiano.exploration.chapter06.comments.controller;

import com.giocosmiano.exploration.chapter06.comments.domain.Comment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {

    private final RabbitTemplate rabbitTemplate;

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

     The comment is published to RabbitMQ's learning-spring-boot exchange with a routing key
     of comments.new
     */

    public CommentController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.flatMap(comment ->
                Mono.fromRunnable(() ->
                        rabbitTemplate
                                .convertAndSend(
                                        "learning-spring-boot",
                                        "comments.new",
                                        comment)))
                .log("commentService-publish")
                .then(Mono.just("redirect:/"));
    }
}