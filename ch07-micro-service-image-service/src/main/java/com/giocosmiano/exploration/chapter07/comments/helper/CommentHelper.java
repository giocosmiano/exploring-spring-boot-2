package com.giocosmiano.exploration.chapter07.comments.helper;

import java.util.Collections;
import java.util.List;

import com.giocosmiano.exploration.chapter07.comments.domain.Comment;
import com.giocosmiano.exploration.chapter07.images.domain.Image;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class CommentHelper {

	private final RestTemplate restTemplate;

	CommentHelper(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "defaultComments")
	public List<Comment> getComments(Image image) {
		return restTemplate.exchange(
				"http://COMMENTS/comments/{imageId}",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Comment>>() {}, // this is how to get around type-erasure in java
				image.getId()).getBody();

	}

	public List<Comment> defaultComments(Image image) {
		return Collections.emptyList();
	}
}
