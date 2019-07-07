### Running `WebSocketsChatApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch08-web-sockets-chat-service-0.0.1-SNAPSHOT.jar
```

 - [Actuator](http://localhost:9072/actuator)
 - [Web Sockets Config for Chat Service](http://localhost:9078/chat-service/default) 

### Brokering WebSocket messages
 - A `WebSocket` is a very lightweight, two-way channel between a web page and the server. `WebSockets`, on
   their own, don't dictate much about what travels over this thin pipe, but one thing is for certain--​ each
   web page, when connected to a server, has a separate session

 - `Spring WebFlux` provides an API that lets us hook into this `WebSocket`-oriented session, whether to
   transmit or receive. But no `WebSocket` session is immediately linked to another `WebSocket` session. If
   we were using `Spring Framework 4's WebSocket API`, we would be leveraging its most sophisticated
   Messaging API. This API was born in Spring Integration, and is the same concept found in Spring
   Cloud Streams. Spring MVC comes with a built-in broker to help bridge messages between different
   sessions. In essence, a message that originates in one `WebSocket` session must be transmitted to the
   broker where it can then be forwarded to any other `WebSocket` session that might be interested 

### Further readings

 - [Spring Cloud](https://spring.io/projects/spring-cloud)
   - [Spring Cloud Reference Guide v.Finchley.SR3](https://cloud.spring.io/spring-cloud-static/Finchley.SR3/single/spring-cloud.html)
   - [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)
   - [Spring Cloud Netflix v2.1.2.RELEASE](https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.2.RELEASE/single/spring-cloud-netflix.html)

### Referenced frameworks/libraries

















