package com.giocosmiano.exploration.chapter08;

import com.giocosmiano.exploration.chapter08.comments.controller.CommentController;
import com.giocosmiano.exploration.chapter08.comments.domain.Comment;
import com.giocosmiano.exploration.chapter08.images.repository.ImageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Profile("simulator")
@Component
public class CommentSimulator {

	private final HomeController homeController;
	private final CommentController commentController;
	private final ImageRepository repository;

	private final AtomicInteger counter;

	public CommentSimulator(HomeController homeController,
                            CommentController commentController,
                            ImageRepository repository) {
		this.homeController = homeController;
		this.commentController = commentController;
		this.repository = repository;
		this.counter = new AtomicInteger(1);
	}

	/*
	 @Profile annotation indicates that this component is only active when
	 spring.profiles.active=simulator is set in the environment variables

	 simulateActivity() is triggered when Spring Boot generates an ApplicationReadyEvent

	 Flux generates a tick every 1000 ms. This tick is transformed into a request for all images, and
	 then a new comment is created against each one, simulating user activity

	 simulateUsersClicking() is also triggered by the same ApplicationReadyEvent. It has a different
	 Flux that simulates a user loading the home page every 500 ms

	 In both of these simulation flows, the downstream activity needs to be wrapped in a Mono.defer
	 in order to provide a target Mono for the downstream provider to subscribe to

	 Both of these Reactor flows must be subscribed to, or they will never run
	 */
	@EventListener
	public void simulateComments(ApplicationReadyEvent event) {
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
								commentController.addComment(newComment)))
				.subscribe();
	}

	@EventListener
	public void simulateUsersClicking(ApplicationReadyEvent event) {
		Flux
				.interval(Duration.ofMillis(500))
				.flatMap(tick ->
						Mono.defer(() ->
								homeController.index(new BindingAwareModelMap())))
				.subscribe();
	}
}
