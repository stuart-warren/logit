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
import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.fields.LocationField;
import com.stuartwarren.logit.fields.ExceptionField.EF;
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
        
        // get exception details
        exceptionInformation(event);
        log.addField(ROOT.EXCEPTION, ExceptionField.get(ROOT.EXCEPTION));
        ExceptionField.clear();
        
        // get location details
        locationInformation(event);
        log.addField(ROOT.LOCATION, LocationField.get(ROOT.LOCATION));
        getLocationInfo = false;
        LocationField.clear();
        
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
        ILoggingEvent loggingEvent) {
        final IThrowableProxy throwableInformation = loggingEvent
                .getThrowableProxy();
        if (throwableInformation != null) {
            if (throwableInformation.getClassName() != null) {
                ExceptionField.put(EF.CLASS, throwableInformation.getClassName());
            }
            if (throwableInformation.getMessage() != null) {
                ExceptionField.put(EF.MESSAGE, throwableInformation.getMessage());
            }
            if (throwableInformation.getStackTraceElementProxyArray() != null) {
                String stackTrace = StringUtils.join(
                        throwableInformation.getStackTraceElementProxyArray(), "\n");
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
            ILoggingEvent loggingEvent) {
        if (getLocationInfo) {
            // TODO: May need to change this? 
            info = ((LoggingEvent) loggingEvent).getCallerData()[0];
            LocationField.put(LF.CLASS, info.getClassName());
            LocationField.put(LF.METHOD, info.getMethodName());
            LocationField.put(LF.FILE, info.getFileName());
            LocationField.put(LF.LINE, Integer.toString(info.getLineNumber()));
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
