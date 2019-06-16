package com.giocosmiano.exploration.chapter06.comments;

import com.giocosmiano.exploration.chapter06.comments.controller.CommentController;
import com.giocosmiano.exploration.chapter06.comments.domain.Comment;
import com.giocosmiano.exploration.chapter06.images.repository.ImageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Profile("simulator")
@Component
public class CommentSimulator {
    private final CommentController controller;
    private final ImageRepository repository;
    private final AtomicInteger counter;

    public CommentSimulator(CommentController controller,
                            ImageRepository repository) {
        this.controller = controller;
        this.repository = repository;
        this.counter = new AtomicInteger(1);
    }

    /*
     @Profile annotation indicates that this only operates if spring.profiles.active=simulator is present
     when the app starts

     @Component annotation will allow this class to get picked up by Spring Boot automatically and
     activated

     @EventListener annotation signals Spring to pipe application events issued to the app context. In
     this case, the method is interested in ApplicationReadyEvent s, fired when the application is up and
     operational

     Flux.interval(Duration.ofMillis(1000)) causes a stream of lazy ticks to get fired every 1000 ms, lazily

     By flatMapping over this Flux, each tick is transformed into all images using the ImageRepository

     Each image is used to generate a new, related comment

     Using the injected CommentController , it simulates the newly minted comment being sent in from the
     web

     Configure the runner with `spring.profiles.active=simulator`. See IntelliJ IDEA provides the means
     to set Spring profiles easily

     metrics can be checked at
     http://localhost:8080/application/metrics/comments.consumed
     http://localhost:8080/application/metrics/comments.produced
     */
    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        Flux
                .interval(Duration.ofMillis(1000))
                .flatMap(tick -> repository.findAll())
                .map(image -> {
                    Comment comment = new Comment();
                    comment.setImageId(image.getId());
                    comment.setComment(
                            "Comment #" + counter.getAndIncrement());
                    return Mono.just(comment);
                })
                .flatMap(newComment ->
                        Mono.defer(() ->
                                controller.addComment(newComment))).subscribe();
    }
}