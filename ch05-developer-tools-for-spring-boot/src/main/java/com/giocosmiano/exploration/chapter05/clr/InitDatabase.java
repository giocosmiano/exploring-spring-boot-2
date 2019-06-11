package com.giocosmiano.exploration.chapter05.clr;

import com.giocosmiano.exploration.chapter05.domain.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InitDatabase {

    /*
     Pre-loading our MongoDB data store. For such operations, it's recommended to actually use the blocking API.
     That's because when launching an application, there is a certain risk of a thread lock issue when both the
     web container as well as our hand-written loader are starting up. Since Spring Boot also creates a
     MongoOperations object, we can simply grab hold of that
     */

    @Bean
    CommandLineRunner init(MongoOperations operations) {
        return args -> {
            initImages(operations);
        };
    }

    private void initImages(final MongoOperations operations) {
        operations.dropCollection(Image.class);

        operations.insert(new Image(UUID.randomUUID().toString(),
                "learning-spring-boot-cover.jpg"));

        operations.insert(new Image(UUID.randomUUID().toString(),
                "learning-spring-boot-2nd-edition-cover.jpg"));

        operations.insert(new Image(UUID.randomUUID().toString(),
                "bazinga.png"));

        operations
                .findAll(Image.class)
                .forEach(image -> System.out.println(image.toString()));
    }

    /*
     @Component ensures that this class will be picked up automatically by Spring Boot, and scanned for
     bean definitions.

     @Bean marks the init method as a bean definition requiring a MongoOperations . In turn, it returns a
     Spring Boot CommandLineRunner , of which all are run after the application context is fully formed
     (though in no particular order)

     When invoked, the command-line runner will use MongoOperations , and request that all entries be
     deleted ( dropCollection ). Then it will insert three new Image records. Finally, it will fetch with ( findAll )
     and iterate over them, printing each out
     */
}