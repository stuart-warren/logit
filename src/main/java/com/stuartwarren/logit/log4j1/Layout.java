/**
 * 
 */
package com.stuartwarren.logit.log4j1;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.stuartwarren.logit.fields.ExceptionField;
import com.stuartwarren.logit.fields.ExceptionField.EXCEPTION;
import com.stuartwarren.logit.fields.LocationField;
import com.stuartwarren.logit.fields.LocationField.LOCATION;
import com.stuartwarren.logit.layout.IFrameworkLayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Layout extends org.apache.log4j.Layout implements IFrameworkLayout {
    
    private String layoutType = "log";
    private String detailThreshold = Level.ERROR.toString();
    private String fields;
    private String tags;
    
    private transient final LayoutFactory layoutFactory = new LayoutFactory();
    private transient LayoutFactory layout;
    
    private transient boolean getLocationInfo = false;
    private transient LocationInfo info;
    
    public Layout() {
        super();
        LogitLog.debug("Log4j1 layout in use.");
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.spi.OptionHandler#activateOptions()
     */
    public void activateOptions() {
        this.layout = layoutFactory.createLayout(this.layoutType);
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
     */
    // TODO: Multithreadme
    @Override
    public String format(final LoggingEvent event) {
        final Log log = doFormat(event);
        return this.layout.format(log);
    }
    
    @SuppressWarnings("unchecked")
    private Log doFormat(final LoggingEvent event) {
        final Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.toLevel(this.detailThreshold))) {
            getLocationInfo = true;
        }
        final Map<String, Object> properties = event.getProperties();
        
        final Log log = this.layout.getLog()
                .setTimestamp(event.getTimeStamp())
                .setLevel(level.toString())
                .setLevelInt(level.toInt())
                .setMdc(properties)
                .setNdc(event.getNDC())
                .setLoggerName(event.getLoggerName())
                .setThreadName(event.getThreadName())
                .setMessage(event.getRenderedMessage())
                .setTags(tags)
                .setFields(fields)
                .appendTag("log4j");
        
        // get exception details
        exceptionInformation(event);
        
        // get location details
        locationInformation(event);
        
        // add all registered fields to log
        log.addRegisteredFields();
        
        // Clear locally used custom fields
        ExceptionField.clear();
        LocationField.clear();
        getLocationInfo = false;
        
        return log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected void exceptionInformation(
            final LoggingEvent loggingEvent) {
        if (loggingEvent.getThrowableInformation() != null) {
            final ThrowableInformation throwableInformation = loggingEvent
                    .getThrowableInformation();
            if (throwableInformation.getThrowable().getClass()
                    .getCanonicalName() != null) {
                ExceptionField.put(EXCEPTION.CLASS, throwableInformation.getThrowable().getClass()
                        .getCanonicalName());
            }
            if (throwableInformation.getThrowable().getMessage() != null) {
                ExceptionField.put(EXCEPTION.MESSAGE, throwableInformation.getThrowable().getMessage());
            }
            if (throwableInformation.getThrowableStrRep() != null) {
                final String stackTrace = StringUtils.join(
                        throwableInformation.getThrowableStrRep(), "\n");
                ExceptionField.put(EXCEPTION.STACKTRACE, stackTrace);
            }
        }
    }

    /**
     * Pull out execution location details (if requested). High cost method!
     * 
     * @param loggingEvent
     * @return
     */
    protected void locationInformation(
            final LoggingEvent loggingEvent) {
        if (getLocationInfo) {
            info = loggingEvent.getLocationInformation();
            LocationField.put(LOCATION.CLASS, info.getClassName());
            LocationField.put(LOCATION.METHOD, info.getMethodName());
            LocationField.put(LOCATION.FILE, info.getFileName());
            LocationField.put(LOCATION.LINE, info.getLineNumber());
        }
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Layout#ignoresThrowable()
     */
    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    /**
     * @return the layoutType
     */
    public String getLayoutType() {
        return layoutType;
    }

    /**
     * @param layoutType the layoutType to set
     */
    public void setLayoutType(final String layoutType) {
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [layoutType] to [" + layoutType + "].");
        }
        this.layoutType = layoutType;
    }

    /**
     * @return the detailThreshold
     */
    public String getDetailThreshold() {
        return detailThreshold;
    }

    /**
     * @param detailThreshold the detailThreshold to set
     */
    public void setDetailThreshold(final String detailThreshold) {
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [detailThreshold] to [" + detailThreshold + "].");
        }
        this.detailThreshold = detailThreshold;
    }

    /**
     * @return the fields
     */
    public String getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(final String fields) {
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("setFields: " + fields);
        }
        this.fields = fields;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(final String tags) {
        this.tags = tags;
    }

}
