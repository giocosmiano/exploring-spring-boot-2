### Running `SpringCloudEurekaServerApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch07-micro-service-discovery-service-0.0.1-SNAPSHOT.jar
```

 - [Eureka](http://localhost:9071/)
 - [Actuator](http://localhost:9071/actuator) 

### Dynamically registering and finding services with Eureka

 - [Netflix Eureka](https://github.com/Netflix/eureka) provides the means for micro services to power up,
   advertise their existence, and shutdown as well. It supports multiple copies of the same service
   registering themselves, and allows multiple instances of Eureka to register with each other to develop
   a highly available service registry.

### Further readings

 - [Spring Cloud](https://spring.io/projects/spring-cloud)

### Referenced frameworks/libraries

















