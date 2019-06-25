## [Exploring Spring Boot 2.0](https://www.packtpub.com/application-development/learning-spring-boot-20-second-edition)
 

### Further readings
 - [Spring Boot](https://spring.io/projects/spring-boot)
 - [Spring Cloud](https://spring.io/projects/spring-cloud)
 - [Spring Data](https://spring.io/projects/spring-data)
 - [Spring Framework Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/index.html)
   - [Core](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core)
   - [Testing](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testing)
   - [Data Access](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#spring-data-tier)
   - [Web on Servlet Stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#spring-web)
   - [Web on Reactive Stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#spring-webflux)
   - [Integration](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#spring-integration)
   - [Language Support](https://docs.spring.io/spring/docs/current/spring-framework-reference/languages.html#languages)
 - [ReactiveX](http://reactivex.io/)
 - [Reactive Streams](http://www.reactive-streams.org/)
 - [Project Reactor - Core ibrary that Spring 5 uses for reactive programming model](https://projectreactor.io/)
 - [Reactive Programming Part I: The Reactive Landscape](http://bit.ly/reactive-part-1)
 - [Reactive Programming Part II: Writing Some Code](http://bit.ly/reactive-part-2)
 - [Reactive Programming Part III: A Simple HTTP Server Application](http://bit.ly/reactive-part-3)
 - [Understanding Reactive types](http://bit.ly/reactive-types)
 - [Reactor Debugging Experience](https://spring.io/blog/2019/03/28/reactor-debugging-experience)
 - [Testing and Debugging Reactor](https://www.cms.lk/testing-debugging-reactor/)

### Dynamically registering and discovering services with Eureka

 - [Netflix Eureka](https://github.com/Netflix/eureka) provides the means for micro services to power up,
   advertise their existence, and shutdown as well. It supports multiple copies of the same service
   registering themselves, and allows multiple instances of Eureka to register with each other to develop
   a highly available service registry.

### Micro-service circuit breakers

 - Annotation, `@EnableCircuitBreaker`, enables [Netflix Hystrix](https://github.com/Netflix/Hystrix),
   the [circuit breaker solution](https://martinfowler.com/bliki/CircuitBreaker.html)
   
 - In short, a circuit breaker is something that, when it detects a certain threshold of failure, will open the
   circuit and prevent any future remote calls for a certain amount of time. The purpose is to prevent
   cascade failures while giving the remote service an opportunity to heal itself and come back online.
   Slamming a service in the middle of startup might be detrimental















