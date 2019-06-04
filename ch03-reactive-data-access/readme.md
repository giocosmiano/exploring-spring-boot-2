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

 - We are granted an out-of-the-box set of `CRUD` operations (save, findById, findAll, delete, deleteById, count, exists, and more).
   We also have the ability to add custom finders purely by method signature ( `findByFirstName` in this example )

 - When Spring Data sees an interface extending its `Repository` marker interface (which `ReactiveCrudRepository`
   does), it creates a concrete implementation. It scans every method, and parses their method signatures.
   Seeing `findBy`, it knows to look at the rest of the method name, and start extracting property names based
   on the domain type ( `Employee` ). Because it can see that `Employee` has `firstName`, it has enough information to
   fashion a query. This also tips it off about expected criteria in the arguments ( `name` ).

 - Finally, `Spring Data` looks at the return type to decide what result set to assemble. The entire query (`NOT` the query
   results), once assembled, is cached, so there is no overhead in using the query multiple times.   

 - `Spring Data's` query-neutral approach is even better. Changing data stores doesn't require throwing away absolutely
   everything and starting over. The interface declared previously extends `Spring Data Commons`, not `Spring Data MongoDB`.
   The only data store details are in the domain object itself.   

 - Instead of `Employee` being some JPA-based entity definition, we can work on a MongoDB document-based one instead
```java
@Data
@Document(collection="employees")
public class Employee {
    @Id String id;
    String firstName;
    String lastName;
}
```

### Extending Spring Data's ReactiveQueryByExampleExecutor

```java
public interface EmployeeRepository extends
        ReactiveCrudRepository<Employee, String>,
        ReactiveQueryByExampleExecutor<Employee> {
}
```

 - `ReactiveQueryByExampleExecutor` interface used to define the repository (provided by Spring Data Commons)
```java
    <S extends T> Mono<S> findOne(Example<S> example);
    <S extends T> Flux<S> findAll(Example<S> example);
```

 - Neither of these aforementioned methods have any properties whatsoever in their names compared to finders like `findByLastName`.
   The big difference is the usage of `Example` as an argument. `Example` is a container provided by `Spring Data Commons` to define
   the parameters of a query

 - Simply put, an `Example` consists of a probe and a matcher. The probe is the POJO object containing all the values we wish to use
   as criteria. The matcher is an `ExampleMatcher` that governs how the probe is used.

 - `Examples`, by default, only query against non-null fields. That's a fancy way of saying that only the fields populated in the
   probe are considered.

 - To illustrate, the probe is hard coded, but in production, the value would be pulled from the request whether it was part of
   a `ReST` route, the body of a web request, or somewhere else

```java
Employee e = new Employee();
e.setFirstName("Bilbo");

Example<Employee> example = Example.of(e);

Mono<Employee> singleEmployee = repository.findOne(example);
```

```java
Employee e = new Employee();
e.setLastName("baggins"); // Lowercase lastName

ExampleMatcher matcher = ExampleMatcher.matching()
  .withIgnoreCase()
  .withMatcher("lastName", startsWith())
  .withIncludeNullValues();

Example<Employee> example = Example.of(e, matcher);

Flux<Employee> multipleEmployees = repository.findAll(example);
```

### Reactive Mongo Operations

 - `MongoTemplate` brings the same power to bear on crafting MongoDB operations. It's very powerful, but there is a critical trade-off.
   All code written using `MongoTemplate` is `MongoDB`-specific. Porting solutions to another data store is very difficult. Hence, it's
   not recommended as the first solution, but as a tool to keep in our back pocket for critical operations that require highly tuned
   `MongoDB` statements.
                   
 - To perform reactive MongoTemplate operations, there is a corresponding `ReactiveMongoTemplate` that supports `Reactor` types.
   The recommended way to interact with `ReactiveMongoTemplate` is through its interface, `ReactiveMongoOperations`
   
 - The tool that actually conducts `MongoDB` repository operations under the hood is, in fact, a `MongoTemplate` (or a `ReactiveMongoTemplate`
   depending on the nature of the repository).
   
 - Some samples of `ReactiveMongoOperations`

```java
Employee e = new Employee();
e.setFirstName("Bilbo");

Example<Employee> example = Example.of(e);

Mono<Employee> singleEmployee = operations.findOne(
  new Query(byExample(example)), Employee.class);
```

```java
Employee e = new Employee();
e.setLastName("baggins"); // Lowercase lastName

ExampleMatcher matcher = ExampleMatcher.matching()
  .withIgnoreCase()
  .withMatcher("lastName", startsWith())
  .withIncludeNullValues();

Example<Employee> example = Example.of(e, matcher);

Flux<Employee> multipleEmployees = operations.find(
  new Query(byExample(example)), Employee.class);
```

### [Spring Data Commons](https://docs.spring.io/spring-data/commons/docs/current/reference/html/)
 - It's the parent project for all [Spring Data](https://spring.io/projects/spring-data) implementations. It defines several concepts
   implemented by every solution. For example, the concept of parsing finder signatures to put together a query request is defined here.
   But the bits where this is transformed into a native query is supplied by the data store solution itself.
   [Spring Data Commons](https://github.com/spring-projects/spring-data-commons) also provides various interfaces, allowing us to reduce
   coupling in our code to the data store, such as `ReactiveCrudRepository`, and others

 - Spring Data blocking APIs support void return types as well. In Reactor-based programming, the equivalent is Mono<Void>,
   because the caller needs the ability to invoke subscribe()
     
### Running `ReactiveDataAccessApplication` from the command line
```
$ java -jar ch03-reactive-data-access-0.0.1-SNAPSHOT.jar
```
 - [Application's main page](http://localhost:9000/)
 - [Images service to get the files](http://localhost:9000/api/images)
 - [Employees service](http://localhost:9000/api/employees)

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
 - [Spring Data lead Oliver Gierke's - Why field injection is evil](http://olivergierke.de/2013/11/why-field-injection-is-evil/)

### Referenced frameworks/libraries
 - [Reactive Web (embedded Netty + Spring WebFlux)](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
 - [Thymeleaf template engine](https://www.thymeleaf.org/)
 - [Lombok - to simplify writing POJOs](https://projectlombok.org/features/all)
 - [Non-Blocking HTTP Multipart parser](https://github.com/synchronoss/nio-multipart)
 - [Spring Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)

### [Installing/Managing MongoDB on Ubuntu 18.04](https://www.digitalocean.com/community/tutorials/how-to-install-mongodb-on-ubuntu-18-04)

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



















