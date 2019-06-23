### Running `SpringCloudImageApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch07-micro-service-image-service-0.0.1-SNAPSHOT.jar
```

 - [Application](http://localhost:9072/)
 - [Actuator](http://localhost:9072/actuator)

### Note on RabbitMQ
 - Normally, when using RabbitMQ, each instance of comments will register its own queue, and
   hence, receive its own copy of newly posted comments. This would result in double posting
   in this scenario. However, Spring Cloud Stream has a solution--`consumer groups`. By
   having `spring.cloud.stream.bindings.input.group=comments` in comments micro service's
   `application.yml`, we declare that only one such queue should receive each individual
   message. This ensures that only one of the micro services actually processes a given event.
   [See for more details](http://docs.spring.io/spring-cloud-stream/docs/Elmhurst.M1/reference/htmlsingle/index.html#consumer-groups)

### Further readings

 - [Spring Cloud](https://spring.io/projects/spring-cloud)
   - [Spring Cloud Reference Guide v.Finchley.SR3](https://cloud.spring.io/spring-cloud-static/Finchley.SR3/single/spring-cloud.html)
   - [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)

### Referenced frameworks/libraries

















