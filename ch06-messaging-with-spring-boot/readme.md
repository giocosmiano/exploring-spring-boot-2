### Running `DevToolsForSpringBootApplication` from the command line
```
$ ./gradlew bootRun
```
 - OR
```
$ java -jar ch06-messaging-with-spring-boot-0.0.1-SNAPSHOT.jar
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



















