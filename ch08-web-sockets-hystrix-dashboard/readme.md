### Running `WebSocketsHystrixDashboardApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch08-web-sockets-hystrix-dashboard-0.0.1-SNAPSHOT.jar
```

 - [Hystrix Dashboard](http://localhost:9079/hystrix)
 - [Web Sockets Config for Hystrix Dashboard](http://localhost:9078/hystrix-dashboard/default) 
 
 - Since each micro service that has `@EnableCircuitBreaker` (pulled in via @SpringCloudApplication) has a
   `/hystrix.stream` endpoint outputting circuit metrics, we can enter that service's URL

### Further readings

 - [Netflix Hystrix](https://github.com/Netflix/Hystrix)

### Referenced frameworks/libraries

















