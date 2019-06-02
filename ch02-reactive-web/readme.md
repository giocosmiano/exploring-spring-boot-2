### Notes
 - Switching from `Embedded Netty` to `Apache Tomcat`

   - By default, `Spring Boot` is geared up to use embedded [Netty](http://netty.io). Why? Because it's one of
     the most popular solutions for reactive applications. And when it comes to reactive applications, it's
     critical that the entire stack be reactive.

 - To switch to another embedded container. We can experiment with using Apache Tomcat and its asynchronous Servlet 3.1 API
   - `spring-boot-starter-webflux` excludes `spring-boot-starter-reactor-netty`, taking it off the classpath
   - `spring-boot-starter-tomcat` is added to the classpath
   - Spring Boot's `TomcatAutoConfiguration` kicks in, and configures the container to work using `TomcatReactiveWebServerFactory`

```
	compile ('org.springframework.boot:spring-boot-starter-webflux') {
		exclude group: 'org.springframework.boot',
			module: 'spring-boot-starter-reactor-netty'
	}
	compile ('org.springframework.boot:spring-boot-starter-tomcat')
```

 - Other containers
   - [Jetty](https://www.eclipse.org/jetty/)
   - [Undertow](http://undertow.io/)

 - Interesting presentation on Java Application Servers
   - [Eberhard Wolff's Java Application Servers Are Dead](https://www.slideshare.net/ewolff/java-application-servers-are-dead)
   
 - Comparing reactive [Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
   against classic [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)

   - [WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) is an alternative module
     in the Spring Framework focused on reactive handling of web requests. A huge benefit is that it uses the same annotations as
     `@MVC`, along with many of the same paradigms while also supporting `Reactor types ( Mono and Flux )` on the inputs and outputs.
     This is NOT available in `Spring MVC`. The big thing to understand is that it's just a module name `spring-webflux` versus `spring-webmvc`

   - `Spring MVC` is built on top of Java EE's Servlet spec. This specification is inherently blocking and synchronous. Asynchronous
     support has been added in later versions, but servlets can still hold up threads in the pool while waiting for responses, defying
     our need for non-blocking. To build a reactive stack, things need to be reactive from top to bottom, and this requires new contracts
     and expectations.
     
   - Certain things, like `HTTP status codes`, a `ResponseBody`, and the `@GetMapping/@PostMapping/@DeleteMapping/@PutMapping`
     annotations are used by both modules. But other things under the hood must be rewritten from scratch. The important point is
     that this does not impact the end developer

   - Switching to `Reactive Spring`, we can immediately start coding with `Flux and Mono`, and don't have to stop and learn a
     totally new web stack. Instead, we can use the popular annotation-based programming model while we invest our effort in
     learning how to make things reactive. It's also important to know that `Spring MVC` isn't going away or slated for end of life.
     Both `Spring WebFlux` and `Spring MVC` will stay as actively supported options inside the Spring portfolio
     
### Services

- [Chapter Service](http://localhost:9000/chapters/)
- [Actuator](http://localhost:9000/actuator)

### Further readings

 - [ReactiveX](http://reactivex.io/)
 - [Reactive Streams](http://www.reactive-streams.org/)
 - [Project Reactor - Core ibrary that Spring 5 uses for reactive programming model](http://projectreactor.io/)
 - [Reactive Programming Part I: The Reactive Landscape](http://bit.ly/reactive-part-1)
 - [Reactive Programming Part II: Writing Some Code](http://bit.ly/reactive-part-2)
 - [Reactive Programming Part III: A Simple HTTP Server Application](http://bit.ly/reactive-part-3)
 - [Understanding Reactive types](http://bit.ly/reactive-types)


### Referenced frameworks/libraries
 - [Reactive Web (embedded Netty + Spring WebFlux)](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
 - [Thymeleaf template engine](https://www.thymeleaf.org/)
 - [Lombok - to simplify writing POJOs](https://projectlombok.org/features/all)
 - [Non-Blocking HTTP Multipart parser](https://github.com/synchronoss/nio-multipart)
 - [Spring Boot Actuator - Production-ready features](https://spring.io/guides/gs/actuator-service/)
 - [Spring Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
