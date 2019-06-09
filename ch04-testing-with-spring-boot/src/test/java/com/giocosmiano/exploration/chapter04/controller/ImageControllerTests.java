package com.giocosmiano.exploration.chapter04.controller;

import com.giocosmiano.exploration.chapter04.domain.Image;
import com.giocosmiano.exploration.chapter04.service.ImageService;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ImageController.class)
@Import({ThymeleafAutoConfiguration.class})
public class ImageControllerTests {

    /*
     @RunWith(SpringRunner.class) ensures all of our Spring Framework and Spring Boot
     test annotations integrate properly with JUnit

     @WebFluxTest(controllers = ImageController.class) is another slice of testing which
     focuses on Spring WebFlux. The default configuration enables all @Controller beans
     and @RestController beans as well as a mock web environment, but with the rest of the
     auto-configuration disabled. However, by using the controllers argument, we have
     confined this test case to ONLY enable ImageController

     @Import(...​ ) specifies what additional bits we want configured outside of any Spring
     WebFlux controllers. In this case, the Thymeleaf auto-configuration is needed

     A WebTestClient bean is autowired into our test case, giving us the means to make mock
     web calls

     @MockBean signals that the ImageService collaborator bean needed by our ImageController
     will be replaced by a mock
     */
    @Autowired
    WebTestClient webClient;

    @MockBean
    ImageService imageService;

    /*
     webClient is used to perform a GET / using its fluent API

     Verify the HTTP status to be a 200 OK, and extract the body of the result into a string

     Use Mockito's verify to prove that our ImageService bean's findAllImages was indeed called

     Use Mockito's verifyNoMoreInteractions to prove that no other calls are made to our mock
     ImageService

     Use AssertJ to inspect some key parts of the HTML page that was rendered
     */
    @Test
    public void baseRouteShouldListAllImages() {
        // given
        Image alphaImage = new Image("1", "alpha.png");
        Image bravoImage = new Image("2", "bravo.png");
        given(imageService.findAllImages())
                .willReturn(Flux.just(alphaImage, bravoImage));

        // when
        EntityExchangeResult<String> result = webClient
                .get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();// then

        verify(imageService).findAllImages();
        verifyNoMoreInteractions(imageService);
        assertThat(result.getResponseBody())
                .contains(
                        "<title>Learning Spring Boot: Spring-a-Gram</title>")
                .contains("<a href=\"/images/alpha.png/raw\">")
                .contains("<a href=\"/images/bravo.png/raw\">");
    }

    @Test
    public void fetchingImageShouldWork() {
        given(imageService.findOneImageResource(any()))
                .willReturn(Mono.just(
                        new ByteArrayResource("data".getBytes())));

        webClient
                .get().uri("/images/alpha.png/raw")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("data");

        verify(imageService).findOneImageResource("alpha.png");
        verifyNoMoreInteractions(imageService);
    }

    @Test
    public void fetchingNullImageShouldFail() throws IOException {
        Resource resource = mock(Resource.class);

        given(resource.getInputStream())
                .willThrow(new IOException("Bad file"));
        given(imageService.findOneImageResource(any()))
                .willReturn(Mono.just(resource));

        webClient
                .get().uri("/images/alpha.png/raw")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Couldn't find alpha.png => Bad file");

        verify(imageService).findOneImageResource("alpha.png");
        verifyNoMoreInteractions(imageService);
    }

    @Test
    public void deleteImageShouldWork() {
        Image alphaImage = new Image("1", "alpha.png");
        given(imageService.deleteImage(any())).willReturn(Mono.empty());

        webClient
                .delete().uri("/images/alpha.png")
                .exchange()
                .expectStatus().isSeeOther()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/");

        verify(imageService).deleteImage("alpha.png");
        verifyNoMoreInteractions(imageService);
    }

}