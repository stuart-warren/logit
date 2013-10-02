logit
=====

[![Build Status](https://travis-ci.org/stuart-warren/logit.png?branch=master)](https://travis-ci.org/stuart-warren/logit)

Library to extend Log4J1.2 by providing a json layout (for logstash) and a zeromq appender (jeromq)
This is my first real attack at a java project, so you have been warned!

v0.0.3

Add to project and use the layout and appender:
```
log4j.rootLogger=DEBUG, A3
log4j.appender.A3=com.stuartwarren.logit.log4j1.zmq.ZmqAppender
log4j.appender.A3.SocketType=PUSHPULL
log4j.appender.A3.Endpoints=tcp://localhost:2120,tcp://localhost:2121
log4j.appender.A3.BindConnect=CONNECT
log4j.appender.A3.SendHWM=1000
log4j.appender.A3.Linger=1
log4j.appender.A3.layout=com.stuartwarren.logit.log4j1.logstash.LogstashV1Layout
```
__Configure ZMQ__

* SocketType: PUSHPULL | PUBSUB (http://zguide.zeromq.org/page:all#Messaging-Patterns)
* BindConnect: BIND | CONNECT (http://zguide.zeromq.org/page:all#Plugging-Sockets-into-the-Topology)
* Linger (Time to wait to close socket http://api.zeromq.org/3-2:zmq-setsockopt#toc13)
* SendHWM (Messages to allow to queue up before block/drop http://api.zeromq.org/3-2:zmq-setsockopt#toc3)
* Endpoints: protocol://hostname:port,protocol://hostname:port (Comma separated list)

In your code, use log4j as normal (add objects to the MDC)
```
Map<String,Object> metricMap = new HashMap<String,Object>();
metricMap.put("com.website.www.500Errors", 1);
MDC.put("metrics", metricMap);
MDC.put("tags", "other");
logger.debug("Hello World!");
LogManager.shutdown();
```
and logs should come out in logstash json_event v1 format
```
{"message":"Hello World!","tags":"other","@timestamp":"2013-09-22T16:51:47.950Z","level":"DEBUG","metrics":{"com.website.www.500Errors":1},"thread":"main","logger":"com.stuartwarren.logit.LogIt","@version":"1"}
```
Note: @timestamp is changed to UTC from whatever timezone you are in.

__Maven__

Use repositories:
```
http://nexus.stuartwarren.com/nexus/content/repositories/snapshots/
http://nexus.stuartwarren.com/nexus/content/repositories/releases/
```
And dependancy:
```
<dependency>
  <groupId>com.stuartwarren</groupId>
  <artifactId>logit</artifactId>
  <version>REQUIRED_VERSION</version>
</dependency>
```
