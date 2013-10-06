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
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.LocationInformation;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Layout extends LayoutBase<ILoggingEvent> {
    
    private String layoutType = "log";
    private Log log;
    private LayoutFactory layoutFactory;
    private LayoutFactory layout;
    
    private String detailThreshold = Level.ERROR.toString();
    private boolean getLocationInfo = false;
    private StackTraceElement info;
    private LocationInformation locationInfo;
    private ExceptionInformation exceptionInfo;   
    
    public Layout() {
        layoutFactory = new LayoutFactory();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.spi.OptionHandler#activateOptions()
     */
    @Override
    public void start() {
        this.layout = layoutFactory.createLayout(this.layoutType);
        this.log = this.layout.getLog();
    }
    
    /* (non-Javadoc)
     * @see ch.qos.logback.core.Layout#doLayout(java.lang.Object)
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        this.log = doFormat(event);
        String stringLog = this.layout.format(this.log);
        return stringLog;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Log doFormat(ILoggingEvent event) {
        this.log.setTimestamp(event.getTimeStamp());
        Level level = event.getLevel();
        if (level.isGreaterOrEqual(Level.toLevel(this.detailThreshold))) {
            getLocationInfo = true;
        }
        this.log.setLevel(level.toString());
        this.log.setLevel_int(level.toInt());
        Map<String, String> properties = event.getMDCPropertyMap();
        this.log.setMdc((Map)properties);
        this.log.setExceptionInformation(exceptionInformation(event));
        this.log.setLocationInformation(locationInformation(event));
        this.log.setLoggerName(event.getLoggerName());
        this.log.setThreadName(event.getThreadName());
        this.log.setMessage(event.getFormattedMessage());
        return this.log;
    }
    
    /**
     * Pull out details of exception in a Hashmap (if they exist)
     * 
     * @param loggingEvent
     * @return
     */
    protected ExceptionInformation exceptionInformation(
            ILoggingEvent loggingEvent) {
        if (loggingEvent.hasCallerData()) {
            exceptionInfo = new ExceptionInformation();
            final IThrowableProxy throwableInformation = loggingEvent
                    .getThrowableProxy();
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

}
