package com.giocosmiano.exploration.chapter07.comments.service;

import com.giocosmiano.exploration.chapter07.comments.domain.Comment;
import com.giocosmiano.exploration.chapter07.comments.processor.CustomProcessor;
import com.giocosmiano.exploration.chapter07.comments.repository.CommentRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(CustomProcessor.class)
public class CommentService {

    /*
     We need to add @EnableBinding(CustomProcessor.class). If this was the only Spring
     Cloud Stream component, we could have used @EnableBinding(Processor.class), however,
     we can't share the same channel, output, with the CommentController. So we need to
     code a custom set of channels
     */

    private CommentRepository repository;
    private final MeterRegistry meterRegistry;

    public CommentService(CommentRepository repository,
                          MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    /*
     The @RabbitListener annotation has been replaced with @StreamListener, indicating that it's transport-
     agnostic

     The argument newComments is tied to the input channel via the @Input() annotation

     Marking it with Flux, we can immediately consume it with our MongoDB repository

     Since we have to hand a stream back to the framework, we have marked up the whole method with
     @Output

     From there, we can flatMap it to generate metrics and then transform it into a Flux of Mono<Void> s
     with Mono.empty() . This ensures that no more processing is done by the framework

     This method has the same concept as all Spring @*Listener annotations--​ invoke the method with optional
     domain objects. But this time, it receives them from whatever underlying technology we have
     configured Spring Cloud Stream to use. The benefit is that this is slim and easy to manage and our code
     is no longer bound to RabbitMQ directly

     Jackson2JsonMessageConverter bean to handle serialization is no longer needed. Spring Cloud Stream
     uses Esoteric Software's Kryo library for serialization/deserialization,
     https://github.com/EsotericSoftware/kryo.
     */
    @StreamListener
    @Output(CustomProcessor.OUTPUT)
    public Flux<Void> save(@Input(CustomProcessor.INPUT)
                                   Flux<Comment> newComments) {
        return repository
                .saveAll(newComments)
                .log("commentService-save")
                .flatMap(comment -> {
                    meterRegistry
                            .counter("comments.consumed", "imageId", comment.getImageId())
                            .increment();
                    return Mono.empty();
                });
    }

    /*
     This code is handy for development, but should be either removed in production or wrapped in a
     @Profile("dev") annotation such that it ONLY runs when spring.profiles.active=dev is present
     */
    @Bean
    CommandLineRunner setUpComments(CommentRepository repository) {
        return args -> {
            repository.deleteAll().subscribe();
        };
    }
}
