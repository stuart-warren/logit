/**
 * 
 */
package com.stuartwarren.logit.logback;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.LayoutBase;

import com.stuartwarren.logit.fields.ExceptionField;
import com.stuartwarren.logit.fields.LocationField;
import com.stuartwarren.logit.fields.ExceptionField.EXCEPTION;
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
public class Layout extends LayoutBase<ILoggingEvent> implements IFrameworkLayout {
    
    private String layoutType = "log";
    private String detailThreshold = Level.ERROR.toString();
    private String fields;
    private String tags;
    
    private transient final LayoutFactory layoutFactory = new LayoutFactory();
    private transient LayoutFactory layout;
    
    private transient boolean getLocationInfo = false;
    private transient StackTraceElement info;  
    
    public Layout() {
        super();
        LogitLog.debug("Logback layout in use.");
    }

    @Override
    public void start() {
        this.layout = layoutFactory.createLayout(this.layoutType);
    }
    
    /* (non-Javadoc)
     * @see ch.qos.logback.core.Layout#doLayout(java.lang.Object)
     */
    public String doLayout(final ILoggingEvent event) {
        final Log log = doFormat(event);
        return this.layout.format(log);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Log doFormat(final ILoggingEvent event) {
        final Log log = this.layout.getLog();
        log.setTimestamp(event.getTimeStamp());
        final Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.toLevel(this.detailThreshold))) {
            getLocationInfo = true;
        }
        log.setLevel(level.toString());
        log.setLevelInt(level.toInt());
        final Map<String, String> properties = event.getMDCPropertyMap();
        log.setMdc((Map)properties);
        
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
        
        log.setLoggerName(event.getLoggerName());
        log.setThreadName(event.getThreadName());
        log.setMessage(event.getFormattedMessage());
        log.setTags(tags);
        log.setFields(fields);
        log.appendTag("logback");
        return log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected void exceptionInformation(
        final ILoggingEvent loggingEvent) {
        final IThrowableProxy throwableInformation = loggingEvent
                .getThrowableProxy();
        if (throwableInformation != null) {
            if (throwableInformation.getClassName() != null) {
                ExceptionField.put(EXCEPTION.CLASS, throwableInformation.getClassName());
            }
            if (throwableInformation.getMessage() != null) {
                ExceptionField.put(EXCEPTION.MESSAGE, throwableInformation.getMessage());
            }
            if (throwableInformation.getStackTraceElementProxyArray() != null) {
                final String stackTrace = StringUtils.join(
                        throwableInformation.getStackTraceElementProxyArray(), "\n");
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
            final ILoggingEvent loggingEvent) {
        if (getLocationInfo) {
            // TODO: May need to change this? 
            info = ((LoggingEvent) loggingEvent).getCallerData()[0];
            LocationField.put(LOCATION.CLASS, info.getClassName());
            LocationField.put(LOCATION.METHOD, info.getMethodName());
            LocationField.put(LOCATION.FILE, info.getFileName());
            LocationField.put(LOCATION.LINE, Integer.toString(info.getLineNumber()));
        }
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
