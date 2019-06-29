## [Exploring Spring Boot 2.0](https://www.packtpub.com/application-development/learning-spring-boot-20-second-edition)
 

### Further readings
 - [Spring Boot](https://spring.io/projects/spring-boot)
 - [Spring Cloud](https://spring.io/projects/spring-cloud)
   - [Spring Cloud Reference Guide v.Finchley.SR3](https://cloud.spring.io/spring-cloud-static/Finchley.SR3/single/spring-cloud.html)
   - [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)
   - [Spring Cloud Netflix v2.1.2.RELEASE](https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.2.RELEASE/single/spring-cloud-netflix.html)
 - [Spring Data](https://spring.io/projects/spring-data)
 - [Spring Framework v.5.x](https://docs.spring.io/spring/docs/current/spring-framework-reference/index.html)
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

### Spring Cloud Config

 - [Spring Cloud Config Server](https://spring.io/projects/spring-cloud-config)
 
 - One thing that quickly adds up when building a micro service-based solution are all the properties that
   must be managed. It's one thing to manage a single application's `application.yml` file, and make tweaks
   and adjustments. But working with all these services, and having to jump to the correct file underneath
   each application's `src/main/resources` folder quickly becomes daunting. On top of that, when trying to
   make changes or adjustments, it is easy to overlook the settings of one micro service
   
 - A key piece of the [Twelve-Factor App](https://12factor.net/) is externalizing configuration. We already
   took a big step using Spring Boot's powerful property support. But Spring Cloud brings another key technology
   to the table that takes property support to the next level--[Spring Cloud Config Server](https://spring.io/projects/spring-cloud-config)
   
 - The `Config Server` let us put all the properties into a centralized location, and feed them via an
   application to our existing micro services
 
 - It's important to note that `spring-cloud-starter-config` is for clients to the Config Server. The
   dependency that was added to the `Config Server` itself was `spring-cloud-starter-config-server`,
   which is only needed to create a `Config Server`  

 - There is a certain order by which Spring Boot launches things. Suffice it to say, property sources must
   be read early in the Spring lifecycle in order to work properly. For this reason, `Spring Cloud Config`
   clients must have a `bootstrap.yml` file

 - By default, `Spring Cloud Config` clients will seek `{spring.application.name}.yml`, so if
  `spring.application.name=eureka` then it'll seek `eureka.yml`   
  
 - To retrieve different versions of configuration settings, we have to set in `bootstrap.yml` to fetch an
   alternative label referring to either a `branch` or a `tag`
   
 - IntelliJ `MultiRun` plugin to group together several launch configurations into a single command.














