### Running `SpringCloudHystrixDashboardApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch07-micro-service-hystrix-dashboard-0.0.1-SNAPSHOT.jar
```

 - [Hystrix Dashboard](http://localhost:9079/hystrix)
 
 - Since each micro service that has `@EnableCircuitBreaker` (pulled in via @SpringCloudApplication) has a
   `/hystrix.stream` endpoint outputting circuit metrics, we can enter that service's URL

### Further readings

 - [Netflix Hystrix](https://github.com/Netflix/Hystrix)

### Referenced frameworks/libraries

















