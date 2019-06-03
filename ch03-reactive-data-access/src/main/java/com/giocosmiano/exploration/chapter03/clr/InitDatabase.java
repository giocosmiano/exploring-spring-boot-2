package com.giocosmiano.exploration.chapter03.clr;

import com.giocosmiano.exploration.chapter03.domain.Employee;
import com.giocosmiano.exploration.chapter03.domain.Image;
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
            initEmployees(operations);
        };
    }

    private void initImages(final MongoOperations operations) {
        operations.dropCollection(Image.class);

        operations.insert(new Image("1",
                "learning-spring-boot-cover.jpg"));

        operations.insert(new Image("2",
                "learning-spring-boot-2nd-edition-cover.jpg"));

        operations.insert(new Image("3",
                "bazinga.png"));

        operations
                .findAll(Image.class)
                .forEach(image -> System.out.println(image.toString()));
    }

    private void initEmployees(final MongoOperations operations) {
        operations.dropCollection(Employee.class);

        operations.insert(new Employee(UUID.randomUUID().toString(), "LeBron", "James", "Basketball Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Michael", "Jordan", "Basketball Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Stephen", "Curry", "Basketball Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Tom", "Brady", "Football Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Payton", "Manning", "Football Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Wayne", "Gretzky", "Hockey Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Sidney", "Crosby", "Hockey Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Lionel", "Messi", "Football Player"));
        operations.insert(new Employee(UUID.randomUUID().toString(), "Christiano", "Ronaldo", "Football Player"));
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