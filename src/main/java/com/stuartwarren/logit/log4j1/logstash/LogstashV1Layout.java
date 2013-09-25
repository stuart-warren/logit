/**
 * 
 */
package com.stuartwarren.logit.log4j1.logstash;

import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import com.stuartwarren.logit.log4j1.CommonLayout;

/**
 * @author Stuart Warren 
 * @date 21 Sep 2013
 *
 */
public class LogstashV1Layout extends CommonLayout {
	
	private String logstashVersion = "1";
    
    public LogstashV1Layout() {
    	super();
    }
    
    /* (non-Javadoc)
	 * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	public String format(LoggingEvent loggingEvent) {
		LogstashV1Log ll = new LogstashV1Log();
		ll.setVersion(logstashVersion);
		Level level = loggingEvent.getLevel();
		addLocationInformation(level);
		ll.setLevel(level.toString());
		@SuppressWarnings("unchecked")
		Map<String, Object> properties = loggingEvent.getProperties();
		ll.setMdc(properties);
		ll.setTimestamp(loggingEvent.getTimeStamp());
		ll.setNdc(loggingEvent.getNDC());
		ll.setExceptionInformation(exceptionInformation(loggingEvent));
		ll.setLocationInformation(locationInformation(loggingEvent));
		ll.setLoggerName(loggingEvent.getLoggerName());
		ll.setThreadName(loggingEvent.getThreadName());
		ll.setMessage(loggingEvent.getRenderedMessage());
		return ll.toString();
	}

}
