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

### Referenced frameworks/libraries


### [Installing/Managing `RabbitMQ` on Ubuntu 18.04](https://www.rabbitmq.com/)

 - RabbitMQ on Ubuntu 18.04
   - [RabbitMQ on Ubuntu 18.04](https://computingforgeeks.com/how-to-install-latest-rabbitmq-server-on-ubuntu-18-04-lts/)
   - [RabbitMQ on Ubuntu](https://tecadmin.net/install-rabbitmq-server-on-ubuntu/)

 - Install pre-requisite [Erlang](https://www.erlang.org/)
```
   $ sudo apt-get update
   $ sudo apt-get install erlang
```

 - [Download from RabbitMQ](https://www.rabbitmq.com/download.html) or just install the package
```
   $ sudo apt-get update
   $ sudo apt-get install rabbitmq-server
```

 - Status of the service,
```
   $ sudo systemctl status rabbitmq-server.service
```

 - Check if service is configured to start on boot,
```
   $ systemctl is-enabled rabbitmq-server.service 
```

 - Stopping the server anytime,
```
   $ sudo systemctl stop rabbitmq-server
```

 - Starting the server when it is stopped,
```
   $ sudo systemctl start rabbitmq-server
```

 - Restarting the server with a single command,
```
   $ sudo systemctl restart rabbitmq-server
```

 - To disable the automatic startup,
```
   $ sudo systemctl disable rabbitmq-server
```

 - To enable automatic start-up,
```
   $ sudo systemctl enable rabbitmq-server
```

 - Enable RabbitMQ Management Dashboard
```
   $ sudo rabbitmq-plugins enable rabbitmq_management
```

 - Creating Admin User in RabbitMQ, admin/secret
```
   $ sudo rabbitmqctl add_user admin secret 
   $ sudo rabbitmqctl set_user_tags admin administrator
   $ sudo rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
```

 - Web service should be listening on TCP port `15672`
```
   http://localhost:15672
```



















