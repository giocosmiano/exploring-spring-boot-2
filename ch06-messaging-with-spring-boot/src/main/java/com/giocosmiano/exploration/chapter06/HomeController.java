package com.giocosmiano.exploration.chapter06;

import com.giocosmiano.exploration.chapter06.comments.repository.CommentReaderRepository;
import com.giocosmiano.exploration.chapter06.images.domain.Image;
import com.giocosmiano.exploration.chapter06.images.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private static final String FILENAME = "{filename:.+}";
    private static final String BASE_PATH = "/images";
    private static final String API_BASE_PATH = "/api";

    private final ImageService imageService;
    private final CommentReaderRepository repository;

    public HomeController(ImageService imageService,
                          CommentReaderRepository repository) {
        this.imageService = imageService;
        this.repository = repository;
    }

    @GetMapping(API_BASE_PATH + "/images")
    @ResponseBody
    Flux<Image> images() {
        return Flux.just(
                new Image("1", "learning-spring-boot-cover.jpg"),
                new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
                new Image("3", "bazinga.png")
        );
    }

    @PostMapping(API_BASE_PATH + "/images")
    Mono<Void> create(@RequestBody Flux<Image> images) {
        return images
                .map(image -> {
                    log.info("We will save " + image +
                            " to a Reactive database soon!");
                    return image;
                })
                .then();
    }

    /*
     The code takes the Flux returned from our ImageService.findAll() method and flatMaps each entry
     from an Image into a call to find related comments

     repository.findByImageId(image.getId()).collectList() actually fetches all Comment objects related to a given
     Image, but turns it into Mono<List<Comment>>. This waits for all of the entries to arrive and bundles them
     into a single object

     The collection of comments and it's related image are bundled together via Mono.zipWith(Mono) ,
     creating a tuple-2 or a pair. (This is the way to gather multiple bits of data and pass them on to the
     next step of any Reactor flow. Reactor has additional tuple types all the way up to Tuple8)

     After flatMapping Flux<Image> into Flux<Tuple2<Image,List<Comment>>>, we then map each entry into a
     classic Java Map to service our Thymeleaf template

     Reactor's Tuple2 has a strongly typed getT1() and getT2(), with T1 being the Image and T2 being the list
     of comments, which is suitable for our needs since it's just a temporary construct used to assemble
     details for the web template

     The image's id and name attributes are copied into the target map from T1

     The comments attribute of our map is populated with the complete List<Comment> extracted from T2

     Since Thymeleaf templates operate on key-value semantics, there is no need to define a
     new domain object to capture this construct. A Java Map will work just fine

     ImageService is fully reactive given that we use MongoDB's reactive drivers.
     The operation to retrieve comments is also reactive. Chaining reactive calls together, using
     Reactor's operators and hitching them to Thymeleaf's reactive solution, ensures that
     everything is being fetched as efficiently as possible and only when necessary. Writing
     reactive apps hinges on having a fully reactive stack
     */
    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images",
                imageService
                        .findAllImages()
                        .flatMap(image ->
                                Mono.just(image)
                                        .zipWith(repository.findByImageId(
                                                image.getId()).collectList()))
                        .map(imageAndComments ->
                                new HashMap<String, Object>(){{
                                    put("id", imageAndComments.getT1().getId());
                                    put("name", imageAndComments.getT1().getName());
                                    put("comments",
                                            imageAndComments.getT2());
                                }})
        );

        model.addAttribute("extra",
                "DevTools can also detect code changes too");
        return Mono.just("index").log("index");
    }

    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw",
            produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(
            @PathVariable String filename) {
        return imageService.findOneImageResource(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(
                                        resource.getInputStream()));
                    } catch (IOException e) {
                        return ResponseEntity.badRequest()
                                .body("Couldn't find " + filename +
                                        " => " + e.getMessage());
                    }
                });
    }

    @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(@RequestPart(name = "file")
                                           Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }

    @DeleteMapping(BASE_PATH + "/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename) {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }
}
