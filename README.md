logit
=====

[![Build Status](https://travis-ci.org/stuart-warren/logit.png?branch=master)](https://travis-ci.org/stuart-warren/logit)

Library to extend Log4J1.2 (and other logging frameworks) by providing a json layout (for logstash) and a zeromq appender (jeromq)
This is my first real attack at a java project, so you have been warned!

v0.2.0

Log4j1
------

Add to project and use the layout and appender:
log4j.properties
```
log4j.rootLogger=TRACE, A2
# A2 is the ZMQ appender with a Logstash json_event v1 layout
log4j.appender.A2=com.stuartwarren.logit.log4j1.ZmqAppender
log4j.appender.A2.SocketType=PUSHPULL
log4j.appender.A2.Endpoints=tcp://localhost:2120
log4j.appender.A2.BindConnect=CONNECT
log4j.appender.A2.Linger=1000
log4j.appender.A2.SendHWM=1000
log4j.appender.A2.layout=com.stuartwarren.logit.log4j1.Layout
log4j.appender.A2.layout.LayoutType=logstashv1
log4j.appender.A2.layout.DetailThreshold=INFO
log4j.appender.A2.layout.Tags=tag1,tag2,tag3
log4j.appender.A2.layout.Fields=field1:value1,field2:value2,field3:value3
```
__Configure ZMQ__

* SocketType: PUSHPULL | PUBSUB (http://zguide.zeromq.org/page:all#Messaging-Patterns)
* BindConnect: BIND | CONNECT (http://zguide.zeromq.org/page:all#Plugging-Sockets-into-the-Topology)
* Linger (Time to wait to close socket http://api.zeromq.org/3-2:zmq-setsockopt#toc13)
* SendHWM (Messages to allow to queue up before block/drop http://api.zeromq.org/3-2:zmq-setsockopt#toc3)
* Endpoints: protocol://hostname:port,protocol://hostname:port (Comma separated list)

__Add optional tags/fields to every log__

Simply follow this format:
```
log4j.appender.A2.layout.Tags=tag1,tag2,tag3
log4j.appender.A2.layout.Fields=field1:value1,field2:value2,field3:value3
```

In your code, use log4j as normal (add objects to the MDC)
```
Logger logger = Logger.getLogger(YourClass.class.getName());
Map<String,Object> metricMap = new HashMap<String,Object>();
metricMap.put("com.website.www.500Errors", 1);
MDC.put("metrics", metricMap);
MDC.put("tags", "other");
logger.debug("Hello World!");
MDC.clear();
System.exit(0);
```
and logs should come out in logstash json_event v1 format
```
{"message":"Hello World!","tags":"other","@timestamp":"2013-10-06T21:22:09.868Z","level":"DEBUG","metrics":{"com.website.www.500Errors":1},"thread":"main","logger":"com.stuartwarren.test_logit.log4j1.LogIt","@version":"1"}
```
Note: @timestamp is changed to UTC from whatever timezone you are in.

Logback (slf4j)
---------------

Initial work on logback appender/layout complete

logback.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
  <appender name="ZMQ" class="com.stuartwarren.logit.logback.ZmqAppender">
    <socketType>PUSHPULL</socketType>
    <endpoints>tcp://localhost:2120</endpoints>
    <bindConnect>CONNECT</bindConnect>
    <linger>1000</linger>
    <sendHWM>1000</sendHWM>
    <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
      <layout class="com.stuartwarren.logit.logback.Layout">
        <layoutType>logstashv1</layoutType>
        <detailThreshold>INFO</detailThreshold>
        <tags>tag1,tag2,tag3</tags>
        <fields>field1:value1,field2:value2,field3:value3</fields>
      </layout>
    </encoder>
  </appender>
  <root level="debug">
    <appender-ref ref="ZMQ" />
  </root>
</configuration>
```
In your code use logback as normal
```
Logger logger = LoggerFactory.getLogger(Logit.class);
# Cannot put objects in slf4j MDC...
MDC.put("tags", "other");
logger.debug("Hello World!");
MDC.clear();
System.exit(0);
```

Other layoutTypes
-----------------

As well as logstashv1:
```
{"message":"There\'s been an error","tags":"other","@timestamp":"2013-10-06T21:40:57.009Z","level":"ERROR","location":{"methodName":"main","fileName":"LogIt.java","lineNumber":"30","className":"com.stuartwarren.test_logit.log4j1.LogIt"},"exception":{"exceptionClass":"java.lang.NullPointerException","exceptionMessage":"Fake error thrown","stackTrace":"java.lang.NullPointerException: Fake error thrown\\n\\tat com.stuartwarren.test_logit.log4j1.LogIt.main(LogIt.java:30)"},"thread":"main","logger":"com.stuartwarren.test_logit.log4j1.LogIt","@version":"1"}
```
There is initial work on:

logstashv0:
```
{"@fields":{"level":"ERROR","location":{"methodName":"main","fileName":"LogIt.java","lineNumber":"30","className":"com.stuartwarren.test_logit.log4j1.LogIt"},"mdc":{"tags":"other"},"exception":{"stackTrace":"java.lang.NullPointerException: Fake error thrown\\n\\tat com.stuartwarren.test_logit.log4j1.LogIt.main(LogIt.java:30)","exceptionClass":"java.lang.NullPointerException","exceptionMessage":"Fake error thrown"},"thread":"main","logger":"com.stuartwarren.test_logit.log4j1.LogIt"},"@timestamp":"2013-10-06T21:41:59.043Z","@message":"There\'s been an error"}
```
gelfv1 - Graylog2 (Still needs work):
```
{"timestamp":"1381095788192","_thread":"main","level":40000,"facility":"com.stuartwarren.test_logit.log4j1.LogIt","file":"LogIt.java","full_message":"java.lang.NullPointerException: Fake error thrown/njava.lang.NullPointerException: Fake error thrown\\n\\tat com.stuartwarren.test_logit.log4j1.LogIt.main(LogIt.java:30)/n","short_message":"There\'s been an error","line":"30","_location":"com.stuartwarren.test_logit.log4j1.LogIt.main(LogIt.java:30)","_tags":"other","version":"1.0"}
```

Just change the layoutType parameter.

Maven
=====

Use repositories:
```
http://nexus.stuartwarren.com/nexus/content/repositories/snapshots/
http://nexus.stuartwarren.com/nexus/content/repositories/releases/
```
And dependency:
``` xml
<dependency>
  <groupId>com.stuartwarren</groupId>
  <artifactId>logit</artifactId>
  <version>REQUIRED_VERSION</version>
</dependency>
```
