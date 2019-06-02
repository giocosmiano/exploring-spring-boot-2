### Query, JPA, jOOQ (java OO Querying)

```sql
-- SQL
SELECT *
FROM EMPLOYEE
WHERE FIRST_NAME = %1

-- JPA
SELECT e
FROM Employee e
WHERE e.firstName = :name

-- jOOQ
create
.select()
.from(EMPLOYEE)
.where(EMPLOYEE.FIRST_NAME.equal(name))
.fetch()
```

### Extending Spring Data's ReactiveCrudRepository

```java
interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {
    Flux<Employee> findByFirstName(Mono<String> name);
}
```

 - We are granted an out-of-the-box set of `CRUD` operations ( save , findById , findAll , delete , deleteById , count , exists , and more).
   We also have the ability to add custom finders purely by method signature ( `findByFirstName` in this example )

 - When Spring Data sees an interface extending its `Repository` marker interface (which `ReactiveCrudRepository`
   does), it creates a concrete implementation. It scans every method, and parses their method signatures.
   Seeing `findBy`, it knows to look at the rest of the method name, and start extracting property names based
   on the domain type ( `Employee` ). Because it can see that `Employee` has `firstName`, it has enough information to
   fashion a query. This also tips it off about expected criteria in the arguments ( `name` ).

 - Finally, Spring Data looks at the return type to decide what result set to assemble--in this case, a `Reactor Flux` that
   we started to explore in the previous chapter. The entire query (`NOT` the query results), once assembled, is cached,
   so, there is no overhead in using the query multiple times.   

 - `Spring Data's` query-neutral approach is even better. Changing data stores doesn't require throwing away absolutely
   everything and starting over. The interface declared previously extends `Spring Data Commons`, not `Spring Data MongoDB`.
   The only data store details are in the domain object itself.   

 - Instead of Employee being some JPA-based entity definition, we can work on a MongoDB document-based one instead
```java
@Data
@Document(collection="employees")
public class Employee {
    @Id String id;
    String firstName;
    String lastName;
}
```

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
 - [How to Install MongoDB on Ubuntu 18.04](https://www.digitalocean.com/community/tutorials/how-to-install-mongodb-on-ubuntu-18-04)

### Referenced frameworks/libraries
 - [Reactive Web (embedded Netty + Spring WebFlux)](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
 - [Thymeleaf template engine](https://www.thymeleaf.org/)
 - [Lombok - to simplify writing POJOs](https://projectlombok.org/features/all)
 - [Non-Blocking HTTP Multipart parser](https://github.com/synchronoss/nio-multipart)
 - [Spring Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)

### Managing MongoDB Service in Ubuntu

 - MongoDB installs as a `systemd` service, which means that we can manage it using standard `systemd` commands alongside all other system services in Ubuntu.

 - Status of the service,
```
   $ sudo systemctl status mongodb
```

 - Stopping the server anytime,
```
   $ sudo systemctl stop mongodb
```

 - Starting the server when it is stopped,
```
   $ sudo systemctl start mongodb
```

 - Restarting the server with a single command,
```
   $ sudo systemctl restart mongodb
```

 - To disable the automatic startup,
```
   $ sudo systemctl disable mongodb
```

 - To enable automatic start-up,
```
   $ sudo systemctl enable mongodb
```



















