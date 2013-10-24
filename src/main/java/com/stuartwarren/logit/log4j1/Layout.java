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
import com.stuartwarren.logit.fields.ExceptionField.EF;
import com.stuartwarren.logit.fields.Field.RF;
import com.stuartwarren.logit.fields.LocationField;
import com.stuartwarren.logit.fields.LocationField.LF;
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
    
    private Log log;
    private LayoutFactory layoutFactory;
    private LayoutFactory layout;
    
    private boolean getLocationInfo = false;
    private LocationInfo info;
    
    public Layout() {
        LogitLog.debug("Log4j1 layout in use.");
        layoutFactory = new LayoutFactory();
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
    @Override
    public String format(LoggingEvent event) {
        this.log = doFormat(event);
        String stringLog = this.layout.format(this.log);
        return stringLog;
    }
    
    @SuppressWarnings("unchecked")
    private Log doFormat(LoggingEvent event) {
        Log log = this.layout.getLog();
        log.setTimestamp(event.getTimeStamp());
        Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.toLevel(this.detailThreshold))) {
            getLocationInfo = true;
        }
        log.setLevel(level.toString());
        log.setLevel_int(level.toInt());
        Map<String, Object> properties = event.getProperties();
        log.setMdc(properties);
        log.setNdc(event.getNDC());
        
        // get exception details
        exceptionInformation(event);
        log.addField(RF.EXCEPTION, ExceptionField.getContext());
        ExceptionField.clear();
        
        // get location details
        locationInformation(event);
        log.addField(RF.LOCATION, LocationField.getContext());
        getLocationInfo = false;
        LocationField.clear();
        
        log.setLoggerName(event.getLoggerName());
        log.setThreadName(event.getThreadName());
        log.setMessage(event.getRenderedMessage());
        log.setTags(tags);
        log.setFields(fields);
        log.appendTag("log4j");
        return log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected void exceptionInformation(
            LoggingEvent loggingEvent) {
        if (loggingEvent.getThrowableInformation() != null) {
            final ThrowableInformation throwableInformation = loggingEvent
                    .getThrowableInformation();
            if (throwableInformation.getThrowable().getClass()
                    .getCanonicalName() != null) {
                ExceptionField.put(EF.CLASS, throwableInformation.getThrowable().getClass()
                        .getCanonicalName());
            }
            if (throwableInformation.getThrowable().getMessage() != null) {
                ExceptionField.put(EF.MESSAGE, throwableInformation.getThrowable().getMessage());
            }
            if (throwableInformation.getThrowableStrRep() != null) {
                String stackTrace = StringUtils.join(
                        throwableInformation.getThrowableStrRep(), "\n");
                ExceptionField.put(EF.STACKTRACE, stackTrace);
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
            LoggingEvent loggingEvent) {
        if (getLocationInfo) {
            info = loggingEvent.getLocationInformation();
            LocationField.put(LF.CLASS, info.getClassName());
            LocationField.put(LF.METHOD, info.getMethodName());
            LocationField.put(LF.FILE, info.getFileName());
            LocationField.put(LF.LINE, info.getLineNumber());
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
    public void setLayoutType(String layoutType) {
        LogitLog.debug("Setting property [layoutType] to [" + layoutType + "].");
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
    public void setDetailThreshold(String detailThreshold) {
        LogitLog.debug("Setting property [detailThreshold] to [" + detailThreshold + "].");
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
    public void setFields(String fields) {
        LogitLog.debug("setFields: " + fields);
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
    public void setTags(String tags) {
        this.tags = tags;
    }

}
