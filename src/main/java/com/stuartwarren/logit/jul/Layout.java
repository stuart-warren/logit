/**
 * 
 */
package com.stuartwarren.logit.jul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.apache.commons.lang3.StringUtils;

import com.stuartwarren.logit.layout.ExceptionInformation;
import com.stuartwarren.logit.layout.IFrameworkLayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.LocationInformation;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Layout extends Formatter implements IFrameworkLayout {
    
    private String layoutType = "log";
    private Log log;
    private LayoutFactory layoutFactory;
    private LayoutFactory layout;
    
    final String prefix = Layout.class.getName();
    private LogManager manager = LogManager.getLogManager();
    
    private String detailThreshold = Level.WARNING.toString();
    private boolean getLocationInfo = false;
    private LocationInformation locationInfo;
    private ExceptionInformation exceptionInfo;
    private String fields;
    private String tags;
    
    
    public Layout() {
        layoutFactory = new LayoutFactory();
        configure();
        this.layout = layoutFactory.createLayout(this.layoutType);
        this.log = this.layout.getLog();
    }
    
    /**
     * 
     */
    private void configure() {
        setLayoutType(manager.getProperty(prefix + ".layoutType"));
        setDetailThreshold(manager.getProperty(prefix + ".detailThreshold"));
        setFields(manager.getProperty(prefix + ".fields"));
        setTags(manager.getProperty(prefix + ".tags"));
    }

    /* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(LogRecord record) {
        this.log = doFormat(record);
        String stringLog = this.layout.format(this.log);
        return stringLog;
    }
    
    private Log doFormat(LogRecord event) {
        this.log.setTimestamp(event.getMillis());
        Level level = event.getLevel();
        if (level.intValue() >= (Level.parse(this.detailThreshold).intValue())) {
            getLocationInfo = true;
        }
        this.log.setLevel(level.toString());
        this.log.setLevel_int(level.intValue());
        Map<String, Object> properties = new HashMap<String,Object>();
        try {
            List<Object> propertiesList = new ArrayList<Object>(Arrays.asList(event.getParameters()));
            properties.put("properties", propertiesList);
            this.log.setMdc(properties);
        } catch (NullPointerException e) {}
        this.log.setExceptionInformation(exceptionInformation(event));
        this.log.setLocationInformation(locationInformation(event));
        this.log.setLoggerName(event.getLoggerName());
        this.log.setThreadName(Integer.toString(event.getThreadID()));
        this.log.setMessage(event.getMessage());
        this.log.setTags(tags);
        this.log.setFields(fields);
        this.log.appendTag("jul");
        return this.log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected ExceptionInformation exceptionInformation(
            LogRecord loggingEvent) {
        if (loggingEvent.getThrown() != null) {
            exceptionInfo = new ExceptionInformation();
            final Throwable throwableInformation = loggingEvent
                    .getThrown();
            if (throwableInformation.getClass() != null) {
                exceptionInfo.setExceptionClass(throwableInformation.getClass().getCanonicalName());
            }
            if (throwableInformation.getMessage() != null) {
                exceptionInfo.setExceptionMessage(throwableInformation.getMessage());
            }
            if (throwableInformation.getStackTrace() != null) {
                String stackTrace = StringUtils.join(
                        throwableInformation.getStackTrace(), "\n");
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
            LogRecord loggingEvent) {
        if (getLocationInfo) {
            locationInfo = new LocationInformation();
            locationInfo.setClassName(loggingEvent.getSourceClassName());
            locationInfo.setMethodName(loggingEvent.getSourceMethodName());
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
