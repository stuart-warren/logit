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
```
Map<String,Object> metricMap = new HashMap<String,Object>();
metricMap.put("com.website.www.500Errors", 1);
MDC.put("metrics", metricMap);
MDC.put("tags", "other");
logger.debug("Hello World!");
```
and logs should come out in logstash json_event v1 format
``{"message":"Hello World!","tags":"other","@timestamp":"2013-09-22T16:51:47.950Z","level":"DEBUG","metrics":{"com.website.www.500Errors":1},"thread":"main","logger":"com.stuartwarren.logit.LogIt","@version":"1"}``
Note: @timestamp is changed to UTC from whatever timezone you are in.
