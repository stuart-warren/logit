/**
 * 
 */
package com.stuartwarren.logit.jul;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import com.stuartwarren.logit.fields.ExceptionField;
import com.stuartwarren.logit.fields.LocationField;
import com.stuartwarren.logit.fields.ExceptionField.EF;
import com.stuartwarren.logit.fields.Field.RF;
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
public class Layout extends Formatter implements IFrameworkLayout {
    
    private transient final String prefix = Layout.class.getName();
    private transient final LogManager manager = LogManager.getLogManager();
    
    private String layoutType = "log";
    private String detailThreshold = Level.WARNING.toString();
    private String fields;
    private String tags;
    
    private transient final LayoutFactory layoutFactory = new LayoutFactory();
    private transient final LayoutFactory layout;
    
    private transient boolean getLocationInfo = false;

    
    public Layout() {
        super();
        LogitLog.debug("Jul layout in use.");
        configure();
        this.layout = layoutFactory.createLayout(this.layoutType);
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
    public String format(final LogRecord record) {
        final Log log = doFormat(record);
        return this.layout.format(log);
    }
    
    private Log doFormat(final LogRecord event) {
        final Log log = this.layout.getLog();
        log.setTimestamp(event.getMillis());
        final Level level = event.getLevel();
        //LogitLog.debug("Level(int): " + level.intValue());
        //LogitLog.debug("DetailThreshold Level(int): " + Level.parse(this.detailThreshold).intValue());
        if (level.intValue() >= (Level.parse(this.detailThreshold).intValue())) {
            getLocationInfo = true;
        }
        log.setLevel(level.toString());
        log.setLevelInt(level.intValue());
        final Map<String, Object> properties = new ConcurrentHashMap<String,Object>();
        try {
            final List<Object> propertiesList = new ArrayList<Object>(Arrays.asList(event.getParameters()));
            properties.put("properties", propertiesList);
            log.setMdc(properties);
        } catch (NullPointerException e) {}
        
        // get exception details
        exceptionInformation(event);
        log.addField(RF.EXCEPTION, ExceptionField.get(RF.EXCEPTION));
        ExceptionField.clear();
        
        // get location details
        locationInformation(event);
        //LogitLog.debug(LocationField.getContext().toString());
        log.addField(RF.LOCATION, LocationField.get(RF.LOCATION));
        getLocationInfo = false;
        LocationField.clear();
        
        log.setLoggerName(event.getLoggerName());
        log.setThreadName(Integer.toString(event.getThreadID()));
        log.setMessage(event.getMessage());
        log.setTags(tags);
        log.setFields(fields);
        log.appendTag("jul");
        return log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected void exceptionInformation(
            final LogRecord loggingEvent) {
        if (loggingEvent.getThrown() != null) {
            final Throwable throwableInformation = loggingEvent
                    .getThrown();
            if (throwableInformation.getClass() != null) {
                ExceptionField.put(EF.CLASS, throwableInformation.getClass().getCanonicalName());
            }
            if (throwableInformation.getMessage() != null) {
                ExceptionField.put(EF.MESSAGE, throwableInformation.getMessage());
            }
            if (throwableInformation.getStackTrace() != null) {              
                final Writer writer = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(writer);
                throwableInformation.printStackTrace(printWriter);
                ExceptionField.put(EF.STACKTRACE, writer.toString());
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
            final LogRecord loggingEvent) {
        if (getLocationInfo) {
            LocationField.put(LF.CLASS, loggingEvent.getSourceClassName());
            LocationField.put(LF.METHOD, loggingEvent.getSourceMethodName());
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
