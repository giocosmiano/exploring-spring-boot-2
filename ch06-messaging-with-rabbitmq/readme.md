### Running `DevToolsForSpringBootApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch06-messaging-with-rabbitmq-0.0.1-SNAPSHOT.jar
```

### Further readings

 - [Spring AMQP](https://spring.io/projects/spring-amqp)

### Referenced frameworks/libraries


### [Installing/Managing `RabbitMQ` on Ubuntu 18.04](https://www.rabbitmq.com/)

 - [Download Erlang](https://www.erlang.org/downloads) on Ubuntu 18.04, pre-requisite of RabbitMQ,
   or install via command line
   - [Erlang on Ubuntu](https://computingforgeeks.com/how-to-install-latest-erlang-on-ubuntu-18-04-lts/)
   - [Erlang on Ubuntu](https://tecadmin.net/install-erlang-on-ubuntu/)
```
   $ wget -O- https://packages.erlang-solutions.com/ubuntu/erlang_solutions.asc | sudo apt-key add -
   $ echo "deb https://packages.erlang-solutions.com/ubuntu bionic contrib" | sudo tee /etc/apt/sources.list.d/rabbitmq.list

   $ sudo apt update
   $ sudo apt -y install erlang
```

 - RabbitMQ on Ubuntu 18.04
   - [RabbitMQ on Ubuntu](https://computingforgeeks.com/how-to-install-latest-rabbitmq-server-on-ubuntu-18-04-lts/)
   - [RabbitMQ on Ubuntu](https://tecadmin.net/install-rabbitmq-server-on-ubuntu/)

 - [Download RabbitMQ](https://www.rabbitmq.com/download.html) or install via command line
```
   $ wget -O- https://dl.bintray.com/rabbitmq/Keys/rabbitmq-release-signing-key.asc | sudo apt-key add -
   $ wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc | sudo apt-key add -
   $ echo "deb https://dl.bintray.com/rabbitmq/debian $(lsb_release -sc) main" | sudo tee /etc/apt/sources.list.d/rabbitmq.list

   $ sudo apt update
   $ sudo apt -y install rabbitmq-server
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

 - Web service should be listening on default TCP port `15672`
   - [RabbitMQ Management Console](http://localhost:15672)

### AMQP fundamentals

 - Each message sent by a `JMS`-based producer is consumed by just one of the clients of that queue.
   `AMQP`-based producers don't publish directly to queues but to `exchanges` instead. When queues are
   declared, they must be bound to an `exchange`. Multiple queues can be bound to the same `exchange`,
   emulating the concept of topics 

 - `JMS` has message selectors which allow consumers to be selective about the messages they receive from
   either queues or topics. `AMQP` has `routing keys` that behave differently based on the type of the
   `exchange`

 - A `direct exchange` routes messages based on a fixed routing key, often the name of the queue.
   Any consumer that binds their own queue to that `exchange` with a `routing key` will receive a copy
   of each message posted in that `exchange`
   
 - A `topic exchange` allows `routing keys` to have wildcards like `comments.*`. This situation best
   suits clients where the actual `routing key` isn't known until a user provides the criteria.
   For example, imagine a stock-trading application where the user must provide a list of ticker
   symbols he or she is interested in monitoring
   
 - A `fanout exchange` blindly broadcasts every message to every queue that is bound to it, regardless of
   the `routing key`
















