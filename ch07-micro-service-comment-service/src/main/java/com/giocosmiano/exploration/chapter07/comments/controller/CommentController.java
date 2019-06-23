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

	@GetMapping("/comments/{imageId}")
	public Flux<Comment> comments(@PathVariable String imageId) {
		return commentRepository.findByImageId(imageId);
	}
}
