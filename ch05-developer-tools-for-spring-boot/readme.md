### Spring Boot's ability to detect a change in our code and relaunch the embedded container
```groovy
compile 'org.springframework.boot:spring-boot-devtools'
```
 - Disables cache settings for auto-configured components
 
 - When it detects a change in code, it restarts the application, holding onto third-party classes and simply throwing away
   and reloading custom classes
   
 - Activates an embedded [LiveReload](http://livereload.com/) server that can trigger the browser to refresh the page automatically
 
 - Listing of all the disabled components

```java
properties.put("spring.thymeleaf.cache", "false");
properties.put("spring.freemarker.cache", "false");
properties.put("spring.groovy.template.cache", "false");
properties.put("spring.mustache.cache", "false");
properties.put("server.session.persistent", "true");
properties.put("spring.h2.console.enabled", "true");
properties.put("spring.resources.cache-period", "0");
properties.put("spring.resources.chain.cache", "false");
properties.put("spring.template.provider.cache", "false");
properties.put("spring.mvc.log-resolved-exception", "true");
properties.put("server.servlet.jsp.init-parameters.development", "true");
properties.put("spring.reactor.stacktrace-mode.enabled", "true");
```

 - With the LiveReload server running and a [LiveReload plugin](http://livereload.com/extensions/) installed in the browser,
   we can enable `LiveReloading` upon visiting the site. Anytime we update the code, the plugin will essentially click the
   browser's refresh button for us.
   
 - `Restarting versus Reloading`: DevTools provides the ability to restart the application quickly, but it is limited in various
   ways. For example, updating the classpath by adding new dependencies is not picked up. Adding new classes isn't supported.
   For more sophisticated tools that handle these complex use cases, take a look at something such as
   [Spring Loaded](https://github.com/spring-projects/spring-loaded ) or [JRebel](https://jrebel.com/software/jrebel/)

### Connecting IDE to a remotely running application and push code changes over the wire, allowing to automatically make mods and test them immediately

 - Add `spring.devtools.remote.secret=ch05-developer-tools-for-spring-boot` to `application.properties`

 - Push the application to the cloud (Pivotal Web Services in this case with `cf push learning-spring-boot -p build/libs/ch05-developer-tools-for-spring-boot-0.0.1-SNAPSHOT.jar`)
 
 - Instead of running the app locally in IDE, run Spring Boot's `DevToolsForSpringBootApplication` class instead
 
 - Add `https://ch05-developer-tools-for-spring-boot.cfapps.io` (or whatever the app's remote URL is) as a program argument
 
 - Launch `org.springframework.boot.devtools.RemoteSpringApplication` configured runner

### Enabling services from [Actuator](https://spring.io/guides/gs/actuator-service/)
```yaml
# http://localhost:9005/actuator
management:
  endpoints:
# exposing endpoints over http
    web:
      exposure:
        include: "*"
# exposing endpoints over jmx
#    jmx:
#      exposure:
#        include: "*"
```

### Running `DevToolsForSpringBootApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch05-developer-tools-for-spring-boot-0.0.1-SNAPSHOT.jar
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



















