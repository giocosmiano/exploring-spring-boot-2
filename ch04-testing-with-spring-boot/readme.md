### Notes
 - My one take away from this chapter, in addition to exploring testing reactive web and reactive data, is spending a good hour
   or so troubleshooting in what I thought to be an issue but rather a reminder to myself the difference between `server-side MVC`
   vs `client-side MVC`. 

 - I'm now more accustomed to `client-based MVC` working on [Angular 2/4](https://angular.io/) for the past 3yrs, and I totally
   overlooked the fact that some construct in Spring, such as annotations, can trip me up easily and cause a function with
   different outcome. The last time I worked on `server-side MVC` was more than 7yrs ago on a [Grails](https://grails.org/) project.

 - The issue, I thought, is that the [Thymeleaf](https://www.thymeleaf.org/) configuration I had was wrong (or perhaps I have some
   missing property item) that causes `WebFlux` and `Thymeleaf` not working together, thus having the `Controller` to return the
   name of the `page` as oppose to redirecting to the `page` itself.
   
 - As it turns out, I have the `Controller` annotated with `@RestController` rather than `@Controller`. Bottom line, use
   `@RestController` if running a `server-side MVC` model where `Controller` is just pure `ReST` services (no server-side page
   re-direction or templating engine such as `Thymeleaf`). Otherwise use `@Controller` when both `ReST` and/or server-side
   page re-direction is supported.    

### Running `TestingWithSpringBootApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch04-testing-with-spring-boot-0.0.1-SNAPSHOT.jar
```

### Further readings

 - [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
 - [Testing the Web Layer](https://spring.io/guides/gs/testing-web/)
 - [Project Reactor - Core ibrary that Spring 5 uses for reactive programming model](https://projectreactor.io/)
   - [Debugging Reactor](https://projectreactor.io/docs/core/release/reference/#debugging)

### Referenced frameworks/libraries
 - [Reactive Web (embedded Netty + Spring WebFlux)](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
 - [Thymeleaf template engine](https://www.thymeleaf.org/)
 - [Lombok - to simplify writing POJOs](https://projectlombok.org/features/all)
 - [Non-Blocking HTTP Multipart parser](https://github.com/synchronoss/nio-multipart)
 - [Spring Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)



















