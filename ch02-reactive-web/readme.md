### Notes
 - Switching from `Embedded Netty` to `Apache Tomcat`

   - By default, `Spring Boot` is geared up to use embedded [Netty](http://netty.io). It is because it's one of
     the most popular solutions for reactive applications. And when it comes to reactive applications, it's
     critical that the entire stack be reactive.

 - To switch to another embedded container. We can experiment with using Apache Tomcat and its asynchronous Servlet 3.1 API
   - `spring-boot-starter-webflux` excludes `spring-boot-starter-reactor-netty`, taking it off the classpath
   - `spring-boot-starter-tomcat` is added to the classpath
   - Spring Boot's `TomcatAutoConfiguration` kicks in, and configures the container to work using `TomcatReactiveWebServerFactory`

```
	compile ('org.springframework.boot:spring-boot-starter-webflux') {
		exclude group: 'org.springframework.boot',
			module: 'spring-boot-starter-reactor-netty'
	}
	compile ('org.springframework.boot:spring-boot-starter-tomcat')
```

 - Other containers
   - [Jetty](https://www.eclipse.org/jetty/)
   - [Undertow](http://undertow.io/)

 - Interesting presentation on Java Application Servers
   - [Eberhard Wolff's Java Application Servers Are Dead](https://www.slideshare.net/ewolff/java-application-servers-are-dead)
   
 - Comparing reactive [Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
   against classic [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)

   - [WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) is an alternative module
     in the Spring Framework focused on reactive handling of web requests. A huge benefit is that it uses the same annotations as
     `@MVC`, along with many of the same paradigms while also supporting `Reactor types ( Mono and Flux )` on the inputs and outputs.
     This is NOT available in `Spring MVC`. The big thing to understand is that it's just a module name `spring-webflux` versus `spring-webmvc`

   - `Spring MVC` is built on top of Java EE's Servlet spec. This specification is inherently blocking and synchronous. Asynchronous
     support has been added in later versions, but servlets can still hold up threads in the pool while waiting for responses, defying
     our need for non-blocking. To build a reactive stack, things need to be reactive from top to bottom, and this requires new contracts
     and expectations.
     
   - Certain things, like `HTTP status codes`, a `ResponseBody`, and the `@GetMapping/@PostMapping/@DeleteMapping/@PutMapping`
     annotations are used by both modules. But other things under the hood must be rewritten from scratch. The important point is
     that this does not impact the end developer

   - Switching to `Reactive Spring`, we can immediately start coding with `Flux and Mono`, and don't have to stop and learn a
     totally new web stack. Instead, we can use the popular annotation-based programming model while we invest our effort in
     learning how to make things reactive. It's also important to know that `Spring MVC` isn't going away or slated for end of life.
     Both `Spring WebFlux` and `Spring MVC` will stay as actively supported options inside the Spring portfolio
     
### Running `ReactiveWebApplication` from the command line
  - If there are exceptions like the ones below, use the `-Dos.detected.` arguments 
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch02-reactive-web-0.0.1-SNAPSHOT.jar
$ java -jar ch02-reactive-web-0.0.1-SNAPSHOT.jar -Dos.detected.name=linux -Dos.detected.arch=x86_64 -Dos.detected.classifier=linux-x86_64
```
 - [Application's main page](http://localhost:9002/)
 - [Images service to get the files](http://localhost:9002/api/images)

### Further readings

 - [ReactiveX](http://reactivex.io/)
 - [Reactive Streams](http://www.reactive-streams.org/)
 - [Project Reactor - Core ibrary that Spring 5 uses for reactive programming model](https://projectreactor.io/)
   - [Debugging Reactor](https://projectreactor.io/docs/core/release/reference/#debugging)
 - [Reactive Programming Part I: The Reactive Landscape](http://bit.ly/reactive-part-1)
 - [Reactive Programming Part II: Writing Some Code](http://bit.ly/reactive-part-2)
 - [Reactive Programming Part III: A Simple HTTP Server Application](http://bit.ly/reactive-part-3)
 - [Understanding Reactive types](http://bit.ly/reactive-types)
 - [Reactor Debugging Experience](https://spring.io/blog/2019/03/28/reactor-debugging-experience)
 - [Testing and Debugging Reactor](https://www.cms.lk/testing-debugging-reactor/)
 - [Doing Reactive Programming with Spring 5](https://stackify.com/reactive-spring-5/)


### Referenced frameworks/libraries
 - [Reactive Web (embedded Netty + Spring WebFlux)](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
 - [Thymeleaf template engine](https://www.thymeleaf.org/)
 - [Lombok - to simplify writing POJOs](https://projectlombok.org/features/all)
 - [Non-Blocking HTTP Multipart parser](https://github.com/synchronoss/nio-multipart)
 - [Spring Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)


### Issues encountered on my Ubuntu 18.04 x64 when running `ReactiveWebApplication`
```markdown
2019-06-02 08:43:13.554 DEBUG 9062 --- [           main] i.n.util.internal.NativeLibraryLoader    : -Dio.netty.native.workdir: /tmp (io.netty.tmpdir)
2019-06-02 08:43:13.554 DEBUG 9062 --- [           main] i.n.util.internal.NativeLibraryLoader    : -Dio.netty.native.deleteLibAfterLoading: true
2019-06-02 08:43:13.555 DEBUG 9062 --- [           main] i.n.util.internal.NativeLibraryLoader    : -Dio.netty.native.tryPatchShadedId: true
2019-06-02 08:43:13.557 DEBUG 9062 --- [           main] i.n.util.internal.NativeLibraryLoader    : Unable to load the library 'netty_transport_native_epoll_x86_64', trying other loading mechanism.

java.lang.UnsatisfiedLinkError: no netty_transport_native_epoll_x86_64 in java.library.path
	at java.lang.ClassLoader.loadLibrary(ClassLoader.java:1867) ~[na:1.8.0_202]
	at java.lang.Runtime.loadLibrary0(Runtime.java:870) ~[na:1.8.0_202]
	at java.lang.System.loadLibrary(System.java:1122) ~[na:1.8.0_202]
	at io.netty.util.internal.NativeLibraryUtil.loadLibrary(NativeLibraryUtil.java:38) ~[netty-common-4.1.36.Final.jar:4.1.36.Final]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_202]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_202]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_202]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_202]
	at io.netty.util.internal.NativeLibraryLoader$1.run(NativeLibraryLoader.java:369) ~[netty-common-4.1.36.Final.jar:4.1.36.Final]
	at java.security.AccessController.doPrivileged(Native Method) ~[na:1.8.0_202]
	at io.netty.util.internal.NativeLibraryLoader.loadLibraryByHelper(NativeLibraryLoader.java:361) [netty-common-4.1.36.Final.jar:4.1.36.Final]
	at io.netty.util.internal.NativeLibraryLoader.loadLibrary(NativeLibraryLoader.java:339) [netty-common-4.1.36.Final.jar:4.1.36.Final]
	at io.netty.util.internal.NativeLibraryLoader.load(NativeLibraryLoader.java:136) [netty-common-4.1.36.Final.jar:4.1.36.Final]
	at io.netty.channel.epoll.Native.loadNativeLibrary(Native.java:198) [netty-transport-native-epoll-4.1.36.Final.jar:4.1.36.Final]
	at io.netty.channel.epoll.Native.<clinit>(Native.java:61) [netty-transport-native-epoll-4.1.36.Final.jar:4.1.36.Final]
	at io.netty.channel.epoll.Epoll.<clinit>(Epoll.java:38) [netty-transport-native-epoll-4.1.36.Final.jar:4.1.36.Final]
	at java.lang.Class.forName0(Native Method) [na:1.8.0_202]
	at java.lang.Class.forName(Class.java:264) [na:1.8.0_202]
	at reactor.netty.resources.DefaultLoopEpoll.<clinit>(DefaultLoopEpoll.java:47) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.resources.LoopResources.preferNative(LoopResources.java:216) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.resources.DefaultLoopResources.onServerSelect(DefaultLoopResources.java:136) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpResources.onServerSelect(TcpResources.java:188) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerRunOn.configure(TcpServerRunOn.java:56) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerRunOn.configure(TcpServerRunOn.java:44) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerBootstrap.configure(TcpServerBootstrap.java:39) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerBootstrap.configure(TcpServerBootstrap.java:39) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerBootstrap.configure(TcpServerBootstrap.java:39) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerBootstrap.configure(TcpServerBootstrap.java:39) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerBootstrap.configure(TcpServerBootstrap.java:39) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServerBootstrap.configure(TcpServerBootstrap.java:39) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.tcp.TcpServer.bind(TcpServer.java:144) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServerBind.bind(HttpServerBind.java:96) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServerOperator.bind(HttpServerOperator.java:42) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServerOperator.bind(HttpServerOperator.java:42) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServerOperator.bind(HttpServerOperator.java:42) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServerOperator.bind(HttpServerOperator.java:42) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServerOperator.bind(HttpServerOperator.java:42) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServer.bind(HttpServer.java:99) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServer.bindNow(HttpServer.java:128) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at reactor.netty.http.server.HttpServer.bindNow(HttpServer.java:111) [reactor-netty-0.8.8.RELEASE.jar:0.8.8.RELEASE]
	at org.springframework.boot.web.embedded.netty.NettyWebServer.startHttpServer(NettyWebServer.java:87) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.web.embedded.netty.NettyWebServer.start(NettyWebServer.java:68) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext$ServerManager.start(ReactiveWebServerApplicationContext.java:232) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.startReactiveWebServer(ReactiveWebServerApplicationContext.java:130) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.finishRefresh(ReactiveWebServerApplicationContext.java:122) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:552) [spring-context-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:67) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:775) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:316) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1260) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1248) [spring-boot-2.1.5.RELEASE.jar:2.1.5.RELEASE]
	at com.giocosmiano.exploration.chapter02.ReactiveWebApplication.main(ReactiveWebApplication.java:12) [classes/:na]
```

 - Similar issues???
   - [java.lang.UnsatisfiedLinkError: no netty_tcnative_osx_x86_64 in java.library.path](https://github.com/netty/netty-tcnative/issues/331)
   - [How to fix the “Found Netty's native epoll transport in the classpath...](https://stackoverflow.com/questions/40746505/how-to-fix-the-found-nettys-native-epoll-transport-in-the-classpath-but-epoll)
   - [Unable to load the library 'netty_transport_native_kqueue_x86_64'](https://stackoverflow.com/questions/48185518/unable-to-load-the-library-netty-transport-native-kqueue-x86-64-trying-other)
 
 - Possible solution???
   - [Netty Native Transport](https://github.com/netty/netty/wiki/Native-transports)
   - [Netty Native Transport](https://netty.io/wiki/native-transports.html)
   - Essentially, I've added this line to `build.gradle`
```
	compile group: 'io.netty', name: 'netty-transport-native-epoll', version: '4.1.36.Final', classifier: 'linux-x86_64'
```

 - Still have the same exceptions as above but now at least the application is starting
```markdown
2019-06-02 08:56:46.266 DEBUG 10389 --- [           main] i.n.util.internal.NativeLibraryLoader    : Successfully loaded the library /tmp/libnetty_transport_native_epoll_x86_648786809683648995047.so
2019-06-02 08:56:46.289  INFO 10389 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port(s): 9000
2019-06-02 08:56:46.292  INFO 10389 --- [           main] c.g.e.chapter02.ReactiveWebApplication   : Started ReactiveWebApplication in 4.311 seconds (JVM running for 4.75)
2019-06-02 08:56:46.808  INFO 10389 --- [       Thread-5] o.s.b.a.mongo.embedded.EmbeddedMongo     : 2019-06-02T08:56:46.808-0400 I NETWORK  [thread1] connection accepted from 127.0.0.1:58166 #3 (3 connections now open)
2019-06-02 08:56:46.811  INFO 10389 --- [       Thread-5] o.s.b.a.mongo.embedded.EmbeddedMongo     : 2019-06-02T08:56:46.811-0400 I NETWORK  [conn3] received client metadata from 127.0.0.1:58166 conn3: { driver: { name: "mongo-java-driver|mongo-java-driver-reactivestreams", version: "3.8.2|1.9.2" }, os: { type: "Linux", name: "Linux", architecture: "amd64", version: "4.15.0-50-generic" }, platform: "Java/Oracle Corporation/1.8.0_202-b08" }
2019-06-02 08:56:46.816  INFO 10389 --- [ntLoopGroup-2-2] org.mongodb.driver.connection            : Opened connection [connectionId{localValue:3, serverValue:3}] to localhost:40807
```

 - [Netty's building native transports](https://github.com/netty/netty/wiki/Native-transports#building-the-native-transports)
```
$ sudo apt-get install autoconf automake libtool make tar gcc-multilib libaio-dev
```

















