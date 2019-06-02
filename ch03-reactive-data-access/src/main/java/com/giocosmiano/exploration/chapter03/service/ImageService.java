package com.giocosmiano.exploration.chapter03.service;

import com.giocosmiano.exploration.chapter03.domain.Image;
import com.giocosmiano.exploration.chapter03.repository.ImageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    public ImageService(ResourceLoader resourceLoader,
                        ImageRepository imageRepository) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
    }

    /**
     * Pre-load some test images
     *
     * @return Spring Boot {@link CommandLineRunner} automatically run after app context is loaded.
     */
    @Bean
    CommandLineRunner setUp() throws IOException {
        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file",
                    new FileWriter(UPLOAD_ROOT +
                            "/learning-spring-boot-cover.jpg"));

            FileCopyUtils.copy("Test file2",
                    new FileWriter(UPLOAD_ROOT +
                            "/learning-spring-boot-2nd-edition-cover.jpg"));

            FileCopyUtils.copy("Test file3",
                    new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
        };
    }

    public Flux<Image> findAllImages() {
        return imageRepository.findAll();
    }

    public Mono<Image> findOneImage(String filename) {
        return imageRepository.findByName(filename);
    }

    /*
     Project Reactor's Mono.when() is akin to the A+ Promise spec's promise.all() API, that waits until all
     sub-promises are completed before moving forward. Project Reactor can be thought of as promises on
     steroids with many more operations available. In this case, by stringing several operations together
     using then(), you can avoid callback hell while ensuring the flow of how things unfold

     The entire flow is terminated with then() so we can signal when all the files have been processed

     Take note it is using flatMap to turn each file into a promise to both copy the file and save a
     MongoDB record. flatMap is kind of like map and then, but on steroids. map has a signature of
     map(T → V) : V , while flatMap has flatMap(T → Publisher<V>) : V , meaning, it can unwrap the Mono
     and produce the contained value. If you're writing a reactive flow that isn't clicking, check
     if one of your map or then calls needs to be replaced with a flatMap

     If we wanted a certain order to happen, the best construct would be Mono.then() . We can chain multiple
     then calls together, ensuring that a certain uniform state is achieved at each step before moving forward
     */

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .flatMap(file -> {
                    Mono<Image> saveDatabaseImage =
                            imageRepository.save(
                                    new Image(
                                            UUID.randomUUID().toString(),
                                            file.filename()));

                    Mono<Void> copyFile = Mono.just(
                            Paths.get(UPLOAD_ROOT, file.filename())
                                    .toFile())
                            .log("createImage-picktarget")
                            .map(destFile -> {
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-newfile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");

                    // using Mono.when() to combine two separate actions similar to promise.all() in JS
                    return Mono.when(saveDatabaseImage, copyFile);
                })
                .then();
    }

    /*
     First we create a Mono to delete the MongoDB image record. It uses imageRepository to first findByName,
     and then it uses a Java 8 method handle to invoke imageRepository.delete.

     Next, we create a Mono using Mono.fromRunnable to delete the file using Files.deleteIfExists . This delays
     deletion until Mono is invoked.

     To have both of these operations completed together, we join them with Mono.when().

     Since we're not interested in the results, we append a then(), which will be completed when the
     combined Mono is done.
     */

    public Mono<Void> deleteImage(String filename) {
        Mono<Void> deleteDatabaseImage =
                imageRepository
                        .findByName(filename)
                        .flatMap(imageRepository::delete);

        Mono<Void> deleteFile = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(
                        Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return Mono.when(deleteDatabaseImage, deleteFile)
                .then();
    }

    /*
     Traditionally, Runnable objects are started in some multithreaded fashion, and are meant to
     run in the background. In this situation, Reactor is in full control of how it gets started
     through the use of its scheduler. Reactor is also able to ensure that the reactive streams
     complete signal is issued when the Runnable object is done with its work.

     The whole point of these various operations from Project Reactor. We
     declare the desired state, and offload all the work scheduling and thread management to the framework.
     We use a toolkit that is designed from the ground up to support asynchronous, non-blocking operations
     for maximum resource usage. This gives us a consistent, cohesive way to define expected results while
     getting maximum efficiency.
     */
}
