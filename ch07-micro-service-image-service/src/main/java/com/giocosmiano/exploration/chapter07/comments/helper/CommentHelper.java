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

	/*
	 restTemplate.exchange() is the generic method for making remote calls. There are shortcuts such as
	 getForObject() and getForEntity() , but when dealing with generics (such as List<Comment> ), we need to
	 switch to exchange()

	 First argument is the URL to the comments service that we just picked. It has the port number we
	 selected along with the route ( /comments/{imageId} , a template) where we can serve up a list of
	 comments based on the image's ID

	 Second argument is the HTTP verb we wish to use-- GET

	 Third argument is for headers and any body. Since this is a GET , there are none

	 Fourth argument is the return type of the data. Due to limitations of Java's generics and type
	 erasure, we have created a dedicated anonymous class to capture the type details for List<Comment> ,
	 which Spring can use to interact with Jackson to properly deserialize

	 Final argument is the parameter ( image.getId() ) that will be used to expand our URI template's
	 {imageId} field

	 Since exchange() returns a Spring ResponseEntity<T> , we need to invoke the body() method to extract the
	 response body

	 The URL has been revamped into http://COMMENTS/comments/{imageId}. COMMENTS is the logical name that our
	 comments micro service registered itself with in Eureka
	 */
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
