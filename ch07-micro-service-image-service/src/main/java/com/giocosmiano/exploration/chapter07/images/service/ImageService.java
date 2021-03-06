package com.giocosmiano.exploration.chapter07.images.service;

import com.giocosmiano.exploration.chapter07.images.domain.Image;
import com.giocosmiano.exploration.chapter07.images.repository.ImageRepository;
import io.micrometer.core.instrument.MeterRegistry;
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
    private final MeterRegistry meterRegistry;

    public ImageService(ResourceLoader resourceLoader,
                        ImageRepository imageRepository,
                        MeterRegistry meterRegistry) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Pre-load some test images
     *
     * @return Spring Boot {@link CommandLineRunner} automatically run after app context is loaded.
     */
    @Bean
    CommandLineRunner setUpImages() throws IOException {
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
        return imageRepository.findAll().log("findAllImages");
    }

    public Mono<Image> findOneImage(String filename) {
        return imageRepository.findByName(filename).log("findOneImage");
    }

    public Mono<Resource> findOneImageResource(String filename) {
        return Mono.fromSupplier(() ->
                resourceLoader.getResource(
                        "file:" + UPLOAD_ROOT + "/" + filename))
                .log("findOneImageResource");
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

     The meterRegistry.summary("files.uploaded.bytes").record(... ), which creates a new distribution summary named
     files.uploaded.bytes. A distribution summary includes both a name, optional tags, and a value. What is
     registered is both a value and an occurrence. Each time a meter is added, it counts it, and the running
     total is tabulated

     Micrometer is a new project at Pivotal. It's a facade for metrics gathering. Think SLF4J, but for metrics
     instead. It is designed to integrate with lots of metric-gathering systems, including Atlas, Prometheus,
     Datadog, Influx, Graphite, and more. In this case, it's using a memory-based solution
     */

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .flatMap(file -> {
                    Mono<Image> saveDatabaseImage =
                            imageRepository.save(
                                    new Image(
                                            UUID.randomUUID().toString(),
                                            file.filename()))
                                    .log("createImage-save");

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
                            .log("createImage-newFile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");

                    Mono<Void> countFile = Mono.fromRunnable(() -> {
                        meterRegistry
                                .summary("files.uploaded.bytes")
                                .record(Paths.get(UPLOAD_ROOT,
                                        file.filename()).toFile().length());
                    });

                    // using Mono.when() to combine two separate actions similar to promise.all() in JS
                    return Mono.when(saveDatabaseImage, copyFile, countFile)
                            .log("createImage-when");
                })
                .log("createImage-flatMap")
                .then()
                .log("createImage-done");
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
                        .log("deleteImage-find")
                        .flatMap(imageRepository::delete)
                        .log("deleteImage-record");

        Mono<Void> deleteFile = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(
                        Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })
                .log("deleteImage-file")
                .then();

        return Mono.when(deleteDatabaseImage, deleteFile)
                .log("deleteImage-when")
                .then()
                .log("deleteImage-done");
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
