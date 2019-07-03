### Running `WebSocketsCommentApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch08-web-sockets-comment-service-0.0.1-SNAPSHOT.jar
```

 - [Actuator](http://localhost:9071/actuator)
 - [Spring Cloud Config for Comment Service](http://localhost:9078/comment-service/default) 

### Note on RabbitMQ
 - Normally, when using RabbitMQ, each instance of comments will register its own queue, and
   hence, receive its own copy of newly posted comments. This would result in double posting
   in this scenario. However, Spring Cloud Stream has a solution--`consumer groups`. By
   having `spring.cloud.stream.bindings.input.group=comments` in comments micro service's
   `application.yml`, we declare that only one such queue should receive each individual
   message. This ensures that only one of the micro services actually processes a given event.
   [See for more details](http://docs.spring.io/spring-cloud-stream/docs/Elmhurst.M1/reference/htmlsingle/index.html#consumer-groups)

### Micro-service circuit breakers

 - Annotation, `@EnableCircuitBreaker`, enables [Netflix Hystrix](https://github.com/Netflix/Hystrix),
   the [circuit breaker solution](https://martinfowler.com/bliki/CircuitBreaker.html)
   
 - In short, a circuit breaker is something that, when it detects a certain threshold of failure, will open the
   circuit and prevent any future remote calls for a certain amount of time. The purpose is to prevent
   cascade failures while giving the remote service an opportunity to heal itself and come back online.
   Slamming a service in the middle of startup might be detrimental

### [Netflix Hystrix](https://github.com/Netflix/Hystrix)

 - [Hystrix various settings](https://github.com/Netflix/Hystrix/wiki/configuration)
 
 - `Hystrix` commands operate using `Spring AOP` (Aspect Oriented Programming). The standard
   approach is through Java proxies (as opposed to `AspectJ` weaving, which requires extra setup). A well-
   known issue with proxies is that in-class invocations don't trigger the enclosing advice. Hence, the
   `Hystrix` command method must be put inside another Spring bean, and injected into our controller.

 - There is some classic advice to offer when talking about `Hystrix's AOP` advice--be careful
   about using thread locals. However, the recommendation against thread locals is even
   stronger when we are talking about `Reactor`-powered applications, the basis for this entire
   book. That's because [Project Reactor](https://projectreactor.io/) uses work stealing, a well-documented
   concept that involves different threads pulling work down when idle. `Reactor's` scheduler is thread
   agnostic, which means that we don't know where the work is actually being carried out. So
   don't use thread locals when writing `Reactor` applications. This impacts other areas too
   such as `Spring Security`, which uses thread locals to maintain contextual security status
   with `SecurityContextHolder`

 - Also, `Hystrix` tabulates every failure, and only opens the circuit when a certain threshold has been
   breached. One missed remote call isn't enough to switch to an offline state
   
 - `Hystrix's` default setting is 50% failure or higher to open the circuit. Another subtle property is that a
   minimum number of requests must be made to possibly open the circuit. The default is 20, meaning that
   19 failures in a row would not open it. When the circuit is opened, `Hystrix` keeps the circuit open a
   minimum amount of time before looking at the rolling window (default: 5000 ms). `Hystrix` maintains a
   rolling window, by default, 10 seconds split up into 10 buckets. As a new bucket of metrics is gathered,
   the oldest is dropped. This collection of buckets is what is examined when deciding whether or not to
   open the circuit

### Further readings

 - [Spring Cloud](https://spring.io/projects/spring-cloud)
   - [Spring Cloud Reference Guide v.Finchley.SR3](https://cloud.spring.io/spring-cloud-static/Finchley.SR3/single/spring-cloud.html)
   - [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)
   - [Spring Cloud Netflix v2.1.2.RELEASE](https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.2.RELEASE/single/spring-cloud-netflix.html)

### Referenced frameworks/libraries

















