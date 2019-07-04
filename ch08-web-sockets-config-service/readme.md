### Running `WebSocketsConfigApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch08-web-sockets-config-service-0.0.1-SNAPSHOT.jar
```

 - [Web Sockets Config Server](https://spring.io/projects/spring-cloud-config)
 
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

### Further readings

 - [Spring Cloud Config](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.1.3.RELEASE/single/spring-cloud-config.html)

### Referenced frameworks/libraries

















