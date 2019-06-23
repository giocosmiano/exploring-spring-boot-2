package com.giocosmiano.exploration.chapter07;

import com.giocosmiano.exploration.chapter07.comments.controller.CommentController;
import com.giocosmiano.exploration.chapter07.comments.domain.Comment;
import com.giocosmiano.exploration.chapter07.images.repository.ImageRepository;
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
