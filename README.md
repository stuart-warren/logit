logit
=====

[![Build Status](https://travis-ci.org/stuart-warren/logit.png?branch=master)](https://travis-ci.org/stuart-warren/logit)

Library to extend Log4J1.2 (and other logging frameworks) by providing a json layout (for logstash) and a zeromq appender (jeromq)
This is my first real attack at a java project, so you have been warned!

v0.5.0

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

Java.util.logging
-----------------

Initial work on JUL

logging.properties
```
handlers=com.stuartwarren.logit.jul.ZmqAppender

.level= INFO

com.stuartwarren.logit.jul.ZmqAppender.level=INFO
com.stuartwarren.logit.jul.ZmqAppender.socketType=PUSHPULL
com.stuartwarren.logit.jul.ZmqAppender.endpoints=tcp://localhost:2120
com.stuartwarren.logit.jul.ZmqAppender.bindConnect=CONNECT
com.stuartwarren.logit.jul.ZmqAppender.linger=1000
com.stuartwarren.logit.jul.ZmqAppender.sendHWM=1000
com.stuartwarren.logit.jul.ZmqAppender.layout=com.stuartwarren.logit.jul.Layout

com.stuartwarren.logit.jul.Layout.layoutType=logstashv0
com.stuartwarren.logit.jul.Layout.detailThreshold=INFO
com.stuartwarren.logit.jul.Layout.tags=tag1,tag2,tag3
com.stuartwarren.logit.jul.Layout.fields=field1:value1,field2:value2,field3:value3
```

In your code, use java.util.logging as normal
```
// Is it normal to specify the location of config in a system.property?
System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
Logger logger = Logger.getLogger(Logit.class.getName());
logger.log(Level.WARNING, "There's been an error", new NullPointerException("Fake error thrown"));
System.exit(0);
```

Tomcat AccessLog Valve
----------------------

Initial work on Tomcat AccessLog Valve

Put jar-with-dependancies into $JAVA_HOME/lib/ext/
Put logit-tomcatvalve into $CATALINA_HOME/lib/

Add a new Valve into your $CATALINA_BASE/conf/server.xml
``` xml
...
<Valve className="com.stuartwarren.logit.tomcatvalve.ZmqAppender"
       layout="com.stuartwarren.logit.tomcatvalve.Layout"
       socketType="PUSHPULL"
       endpoints="tcp://localhost:2120"
       bindConnect="CONNECT"
       linger="1000"
       sendHWM="1000"
       layoutType="logstashv1"
       iHeaders="Referer,User-Agent"
       oHeaders=""
       cookies=""
       tags="tag1,tag2,tag3"
       fields="field1:value1,field2:value2,field3:value3" />
...
```

Produces logs like:
```
{"message":"GET /etoheubtoe?teethiae=rc,.dudx,.u HTTP/1.1 404","tags":["tag1","tag2","tag3","valve"],"@timestamp":"2013-10-24T20:23:46.222Z","field3":"value3","field2":"value2","level":"ERROR","http":{"request_protocol":"HTTP/1.1","response_headers":{},"request_querystring":"teethiae=rc,.dudx,.u","remote_user":null,"request_headers":{"Referer":null,"User-Agent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36"},"response_size":985,"response_status":404,"request_parameters":{"teethiae":["rc,.dudx,.u"]},"request_uri":"/etoheubtoe","remote_host":"192.168.1.208","server_name":"192.168.1.67","request_method":"GET","response_duration":27,"cookies":{}},"hostname":"precise64","field1":"value1","@version":"1","user":"tomcat7"}
```

Vagrantfile supplied to hopefully make testing easier
```
vagrant up
vagrant ssh
python /vagrant/src/test/resources/test.py &
sudo service tomcat7 restart
wget 'http://localhost:8080/testurl/?param1=value1'
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

Debug mode
----------

Do you need to debug what the code is doing?

Set a system property
```
in code:
System.setProperty("logit.debug", "");

OR during startup:
-Dlogit.debug
```

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
.

