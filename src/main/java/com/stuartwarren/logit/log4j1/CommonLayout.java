/**
 * 
 */
package com.stuartwarren.logit.log4j1;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.stuartwarren.logit.CommonLog;


/**
 * @author Stuart Warren 
 * @date 21 Sep 2013
 *
 */
public class CommonLayout extends Layout {
	

    private boolean ignoreThrowable;
    private boolean locationInfo;
	protected boolean activeIgnoreThrowable;
    
    private LocationInfo info;
    
    public CommonLayout() {
    	locationInfo = false;
    	ignoreThrowable = false;
    	activeIgnoreThrowable = ignoreThrowable;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * @param loggingEvent
     * @return
     */
    protected HashMap<String, Object> exceptionInformation(LoggingEvent loggingEvent) {
    	HashMap<String, Object> exceptionInformation = new HashMap<String, Object>();
    	if (loggingEvent.getThrowableInformation() != null) {
            final ThrowableInformation throwableInformation = loggingEvent.getThrowableInformation();
            if (throwableInformation.getThrowable().getClass().getCanonicalName() != null) {
                exceptionInformation.put("exception_class", throwableInformation.getThrowable().getClass().getCanonicalName());
            }
            if (throwableInformation.getThrowable().getMessage() != null) {
                exceptionInformation.put("exception_message", throwableInformation.getThrowable().getMessage());
            }
            if (throwableInformation.getThrowableStrRep() != null) {
                String stackTrace = StringUtils.join(throwableInformation.getThrowableStrRep(), "\n");
                exceptionInformation.put("stacktrace", stackTrace);
            }
        }
    	return exceptionInformation;
    }
    
    /**
     * Pull out execution location details (if requested). High cost method!
     * @param loggingEvent
     * @return
     */
    protected HashMap<String, Object> locationInformation(LoggingEvent loggingEvent) {
    	HashMap<String, Object> locationInformation = new HashMap<String, Object>();
    	if (locationInfo) {
            info = loggingEvent.getLocationInformation();
            locationInformation.put("file", info.getFileName());
            locationInformation.put("line_number", info.getLineNumber());
            locationInformation.put("class", info.getClassName());
            locationInformation.put("method", info.getMethodName());
        }
    	return locationInformation;
    }

	/* (non-Javadoc)
	 * @see org.apache.log4j.spi.OptionHandler#activateOptions()
	 */
	public void activateOptions() {
		this.activeIgnoreThrowable = ignoreThrowable;
	}
	
	/**
     * Query whether log messages include location information.
     *
     * @return true if location information is included in log messages, false otherwise.
     */
    public boolean getLocationInfo() {
        return locationInfo;
    }
    
    /**
     * Set whether log messages should include location information.
     *
     * @param locationInfo true if location information should be included, false otherwise.
     */
    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

	/* (non-Javadoc)
	 * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	public String format(LoggingEvent loggingEvent) {
		CommonLog cl = new CommonLog();
		cl.setTimestamp(loggingEvent.getTimeStamp());
		Level level = loggingEvent.getLevel();
		cl.setLevel(level.toString());
		@SuppressWarnings("unchecked")
		Map<String,Object> properties = loggingEvent.getProperties();
		cl.setMdc(properties);
		cl.setNdc(loggingEvent.getNDC());
		cl.setExceptionInformation(exceptionInformation(loggingEvent));
		cl.setLocationInformation(locationInformation(loggingEvent));
		cl.setLoggerName(loggingEvent.getLoggerName());
		cl.setThreadName(loggingEvent.getThreadName());
		cl.setMessage(loggingEvent.getRenderedMessage());
		return cl.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Layout#ignoresThrowable()
	 */
	@Override
	public boolean ignoresThrowable() {
		return ignoreThrowable;
	}
	
	/**
	 * If level is greater or equal to WARN, log more detail!
	 * @param level
	 */
	protected void addLocationInformation(Level level) {
		if (level.isGreaterOrEqual(Level.WARN)) {
			setLocationInfo(true);
		}
	}

}
