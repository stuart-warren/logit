logit
=====

Library to extend Log4J1.2 by providing a json layout (for logstash) and a zeromq appender (jeromq)
This is my first real attack at a java project, so you have been warned!

v0.0.1

Add to project and use the layout:
```
log4j.rootLogger=DEBUG, A2
log4j.appender.A2=org.apache.log4j.ConsoleAppender
log4j.appender.A2.layout=com.stuartwarren.logit.log4j1.logstash.LogstashV1Layout
```

In your code, use log4j as normal (add objects to the MDC)
and logs should come out in logstash json_event v1 format

Note: @timestamp is changed to UTC from whatever timezone you are in.
