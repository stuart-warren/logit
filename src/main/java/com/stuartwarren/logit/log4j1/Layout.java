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

import com.stuartwarren.logit.layout.ExceptionInformation;
import com.stuartwarren.logit.layout.IFrameworkLayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.LocationInformation;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Layout extends org.apache.log4j.Layout implements IFrameworkLayout {
    
    private String layoutType = "log";
    private Log log;
    private LayoutFactory layoutFactory;
    private LayoutFactory layout;
    
    private String detailThreshold = Level.ERROR.toString();
    private boolean getLocationInfo = false;
    private LocationInfo info;
    private LocationInformation locationInfo;
    private ExceptionInformation exceptionInfo;
    private String fields;
    private String tags;
    
    
    public Layout() {
        LogitLog.debug("Log4j1 layout in use.");
        layoutFactory = new LayoutFactory();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.spi.OptionHandler#activateOptions()
     */
    @Override
    public void activateOptions() {
        this.layout = layoutFactory.createLayout(this.layoutType);
        this.log = this.layout.getLog();
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
    
    private Log doFormat(LoggingEvent event) {
        this.log.setTimestamp(event.getTimeStamp());
        Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.toLevel(this.detailThreshold))) {
            getLocationInfo = true;
        }
        this.log.setLevel(level.toString());
        this.log.setLevel_int(level.toInt());
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = event.getProperties();
        this.log.setMdc(properties);
        this.log.setNdc(event.getNDC());
        this.log.setExceptionInformation(exceptionInformation(event));
        this.log.setLocationInformation(locationInformation(event));
        this.log.setLoggerName(event.getLoggerName());
        this.log.setThreadName(event.getThreadName());
        this.log.setMessage(event.getRenderedMessage());
        this.log.setTags(tags);
        this.log.setFields(fields);
        this.log.appendTag("log4j");
        return this.log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected ExceptionInformation exceptionInformation(
            LoggingEvent loggingEvent) {
        if (loggingEvent.getThrowableInformation() != null) {
            exceptionInfo = new ExceptionInformation();
            final ThrowableInformation throwableInformation = loggingEvent
                    .getThrowableInformation();
            if (throwableInformation.getThrowable().getClass()
                    .getCanonicalName() != null) {
                exceptionInfo.setExceptionClass(throwableInformation.getThrowable().getClass()
                        .getCanonicalName());
            }
            if (throwableInformation.getThrowable().getMessage() != null) {
                exceptionInfo.setExceptionMessage(throwableInformation.getThrowable().getMessage());
            }
            if (throwableInformation.getThrowableStrRep() != null) {
                String stackTrace = StringUtils.join(
                        throwableInformation.getThrowableStrRep(), "\n");
                exceptionInfo.setStackTrace(stackTrace);
            }
        }
        return exceptionInfo;
    }

    /**
     * Pull out execution location details (if requested). High cost method!
     * 
     * @param loggingEvent
     * @return
     */
    protected LocationInformation locationInformation(
            LoggingEvent loggingEvent) {
        if (getLocationInfo) {
            locationInfo = new LocationInformation();
            info = loggingEvent.getLocationInformation();
            locationInfo.setClassName(info.getClassName());
            locationInfo.setMethodName(info.getMethodName());
            locationInfo.setFileName(info.getFileName());
            locationInfo.setLineNumber(info.getLineNumber());
        }
        return locationInfo;
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
