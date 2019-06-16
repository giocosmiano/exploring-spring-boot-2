package com.giocosmiano.exploration.chapter06.comments.service;

import com.giocosmiano.exploration.chapter06.comments.domain.Comment;
import com.giocosmiano.exploration.chapter06.comments.repository.CommentWriterRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private CommentWriterRepository repository;
    private final MeterRegistry meterRegistry;

    public CommentService(CommentWriterRepository repository,
                          MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    /*
     @RabbitListener annotation is the easiest way to register methods to consume messages

     QueueBinding annotation is the easiest way to declare the queue and the exchange it's bound to
     on-the-fly. In this case, it creates an anonymous queue for this method and binds to the learning-
     spring-boot exchange

     The routing key for this method is comments.new , meaning any message posted to the learning-spring-
     boot exchange with that exact routing key will cause this method to be invoked

     It's possible for the @RabbitListener methods to receive a Spring AMQP Message, a Spring Messaging
     Message, various message headers, as well as a plain old Java object (which is what we have here)

     The method itself invokes our CommentWriterRepository to actually save the comment in the data store

     To use RabbitMQ, we would normally need @EnableRabbit, but thanks to Spring Boot, it's automatically
     activated when spring-boot-starter-amqp is on the classpath. Once again, Boot knows what we want and
     just does it

     It's important to understand that @RabbitListener makes it possible to dynamically create all the
     exchanges and queues needed to operate. However, it only works if an instance of AmqpAdmin is in the
     application context. Without it, ALL exchanges and queues must be declared as separate Spring beans.
     But Spring Boot's RabbitMQ auto-configuration policy provides one

     One slight issue with this method that will cause it to not operate--object serialization. If we had
     declared the method signature to provide us with a Spring AMQP Message object, we would pull down a
     byte array. However, out of the box, Spring AMQP has limited functionality in serializing custom
     domain objects. With no effort, it can handle simple strings and serializables

     Using the injected MeterRegistry, we increment a comments.consumed metric with every comment

     It's also tagged with the comment's related imageId

     The metrics are handled after the save is completed inside the subscribe method. This method grants
     us the ability to execute some code once the flow is complete

     Spring AMQP doesn't yet support Reactive Streams. That is why rabbitTemplate.convertAndSend() must
     be wrapped in Mono.fromRunnable. Blocking calls such as this subscribe() method should be red flags,
     but in this situation, it's a necessary evil until Spring AMQP is able to add support. There is no
     other way to signal for this Reactor flow to execute without it
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = "learning-spring-boot"),
            key = "comments.new"
    ))
    public void save(Comment newComment) {
        repository
                .save(newComment)
                .log("commentService-save")
                .subscribe(comment -> {
                    meterRegistry
                            .counter("comments.consumed", "imageId", comment.getImageId())
                            .increment();
                });
    }

    /*
     @Bean registers this as a bean definition and creates Jackson2JsonMessageConverter, an implementation
     of Spring AMQP's MessageConverter, used to serialize and deserialize Spring AMQP Message objects.
     In this case, is uses Jackson to convert @BeanPOJOs to/from JSON strings

     Spring Boot's RabbitMQ auto-configuration policy will look for any implementation of Spring AMQP's
     MessageConverter instances and register them with both the RabbitTemplate we used earlier as well as the
     SimpleMessageListenerContainer that it creates when it spots @RabbitListener in our code.
     */
    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /*
     This code is handy for development, but should be either removed in production or wrapped in a
     @Profile("dev") annotation such that it ONLY runs when spring.profiles.active=dev is present
     */
    @Bean
    CommandLineRunner setUpComments(MongoOperations operations) {
        return args -> {
            operations.dropCollection(Comment.class);
        };
    }
}
