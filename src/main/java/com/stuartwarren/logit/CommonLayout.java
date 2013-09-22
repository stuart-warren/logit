/**
 * 
 */
package com.stuartwarren.logit;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * @author Stuart Warren 
 * @date 21 Sep 2013
 *
 */
public class CommonLayout extends Layout {
	

    private boolean ignoreThrowable = false;
    private boolean locationInfo = false;
    private boolean activeIgnoreThrowable = ignoreThrowable;
    
    private LocationInfo info;
    private CommonLog cl = new CommonLog();
    
    public CommonLayout() {
    	
    }
    
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
		activeIgnoreThrowable = ignoreThrowable;
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
		cl.setTimestamp(loggingEvent.getTimeStamp());
		cl.setLevel(loggingEvent.getLevel().toString());
		cl.setMdc(loggingEvent.getProperties());
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

}
