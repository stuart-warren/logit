/**
 * 
 */
package com.stuartwarren.logit.log4j1.logstash;

import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.stuartwarren.logit.log4j1.CommonLayout;
import com.stuartwarren.logit.logstash.LogstashV1Log;

/**
 * @author Stuart Warren
 * @date 21 Sep 2013
 */
public class LogstashV1Layout extends CommonLayout {

    private String logstashVersion = "1";

    public LogstashV1Layout() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    public String format(LoggingEvent loggingEvent) {
        LogstashV1Log ll = new LogstashV1Log();
        return doFormat(ll, loggingEvent) + "\n";
    }

    private String doFormat(LogstashV1Log logs, LoggingEvent loggingEvent) {
        logs.setVersion(logstashVersion);
        Level level = loggingEvent.getLevel();
        addLocationInformation(level);
        logs.setLevel(level.toString());
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = loggingEvent.getProperties();
        logs.setMdc(properties);
        logs.setTimestamp(loggingEvent.getTimeStamp());
        logs.setNdc(loggingEvent.getNDC());
        logs.setExceptionInformation(exceptionInformation(loggingEvent));
        logs.setLocationInformation(locationInformation(loggingEvent));
        logs.setLoggerName(loggingEvent.getLoggerName());
        logs.setThreadName(loggingEvent.getThreadName());
        logs.setMessage(loggingEvent.getRenderedMessage());
        return logs.toString();
    }

}
