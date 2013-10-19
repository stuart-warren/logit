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
public class Layout extends LayoutBase<ILoggingEvent> implements IFrameworkLayout {
    
    private String layoutType = "log";
    private String detailThreshold = Level.ERROR.toString();
    private String fields;
    private String tags;
    
    private Log log;
    private LayoutFactory layoutFactory;
    private LayoutFactory layout;
    
    private boolean getLocationInfo = false;
    private StackTraceElement info;
    private LocationInformation locationInfo;
    private ExceptionInformation exceptionInfo;   
    
    public Layout() {
        LogitLog.debug("Logback layout in use.");
        layoutFactory = new LayoutFactory();
    }

    @Override
    public void start() {
        this.layout = layoutFactory.createLayout(this.layoutType);
    }
    
    /* (non-Javadoc)
     * @see ch.qos.logback.core.Layout#doLayout(java.lang.Object)
     */
    public String doLayout(ILoggingEvent event) {
        this.log = doFormat(event);
        String stringLog = this.layout.format(this.log);
        return stringLog;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Log doFormat(ILoggingEvent event) {
        Log log = this.layout.getLog();
        log.setTimestamp(event.getTimeStamp());
        Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.toLevel(this.detailThreshold))) {
            getLocationInfo = true;
        }
        log.setLevel(level.toString());
        log.setLevel_int(level.toInt());
        Map<String, String> properties = event.getMDCPropertyMap();
        log.setMdc((Map)properties);
        log.setExceptionInformation(exceptionInformation(event));
        log.setLocationInformation(locationInformation(event));
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
    protected ExceptionInformation exceptionInformation(
        ILoggingEvent loggingEvent) {
        exceptionInfo = null;
        final IThrowableProxy throwableInformation = loggingEvent
                .getThrowableProxy();
        if (throwableInformation != null) {
            exceptionInfo = new ExceptionInformation();
            if (throwableInformation.getClassName() != null) {
                exceptionInfo.setExceptionClass(throwableInformation.getClassName());
            }
            if (throwableInformation.getMessage() != null) {
                exceptionInfo.setExceptionMessage(throwableInformation.getMessage());
            }
            if (throwableInformation.getStackTraceElementProxyArray() != null) {
                String stackTrace = StringUtils.join(
                        throwableInformation.getStackTraceElementProxyArray(), "\n");
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
            ILoggingEvent loggingEvent) {
        locationInfo = null;
        if (getLocationInfo) {
            locationInfo = new LocationInformation();
            // TODO: May need to change this? 
            info = ((LoggingEvent) loggingEvent).getCallerData()[0];
            locationInfo.setClassName(info.getClassName());
            locationInfo.setMethodName(info.getMethodName());
            locationInfo.setFileName(info.getFileName());
            locationInfo.setLineNumber(Integer.toString(info.getLineNumber()));
        }
        return locationInfo;
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
