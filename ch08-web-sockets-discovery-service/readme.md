### Running `WebSocketsEurekaServerApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch08-web-sockets-discovery-service-0.0.1-SNAPSHOT.jar
```

 - [Eureka](http://localhost:9070/)
 - [Actuator](http://localhost:9070/actuator)
 - [Web Sockets Config for Eureka Discovery/Registry Service](http://localhost:9078/discovery-service/default) 

### Dynamically registering and finding services with Eureka

 - [Netflix Eureka](https://github.com/Netflix/eureka) provides the means for micro services to power up,
   advertise their existence, and shutdown as well. It supports multiple copies of the same service
   registering themselves, and allows multiple instances of Eureka to register with each other to develop
   a highly available service registry.

 - [Spring Cloud Netflix Eureka](https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)

 - [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/single/spring-cloud-netflix.html)
 
### Further readings

 - [Spring Cloud](https://spring.io/projects/spring-cloud)
   - [Spring Cloud Reference Guide v.Finchley.SR3](https://cloud.spring.io/spring-cloud-static/Finchley.SR3/single/spring-cloud.html)
   - [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html)

### Referenced frameworks/libraries

















