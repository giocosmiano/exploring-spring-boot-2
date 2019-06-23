package com.giocosmiano.exploration.chapter07.comments.controller;

import com.giocosmiano.exploration.chapter07.comments.domain.Comment;
import com.giocosmiano.exploration.chapter07.comments.repository.CommentRepository;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

	private final CommentRepository commentRepository;

	public CommentController(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	/*
	 @RestController indicates this is a Spring WebFlux controller where all results are written directly
	 into the HTTP response body

	 CommentRepository is injected into a field using constructor injection

	 @GetMapping() configures this method to respond to GET /comments/{imageId} requests

	 @PathVariable String imageId gives us access to the {imageId} piece of the route

	 Method returns a Flux of comments by invoking our repository's findByImage() using the imageId

	 Having coded things all the way from populating the UI with comments in our `images` service, going
	 through Ribbon and Eureka, to the `comments` service, we are fetching comments from the system
	 responsible for managing them

	 RestTemplate doesn't speak Reactive Streams. It's a bit too old for that. But there is a new
	 remote calling library in Spring Framework 5 called WebClient . Why aren't we using it?
	 Because it doesn't (yet) support Eureka logical hostname resolution. Hence, the part of our
	 application making RestTemplate calls is blocking. In the future, when that becomes
	 available, it is highly recommended migrating to it, based on its fluent API and support for
	 Reactor types
	 */
	@GetMapping("/comments/{imageId}")
	public Flux<Comment> comments(@PathVariable String imageId) {
		return commentRepository.findByImageId(imageId);
	}
}
