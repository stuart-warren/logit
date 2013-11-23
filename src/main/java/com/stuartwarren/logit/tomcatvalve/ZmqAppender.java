/**
 * 
 */
package com.stuartwarren.logit.tomcatvalve;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AccessLogValve;

import com.stuartwarren.logit.layout.IFrameworkLayout;
import com.stuartwarren.logit.utils.LayoutLoader;
import com.stuartwarren.logit.utils.LogitLog;
import com.stuartwarren.logit.zmq.IZmqTransport;
import com.stuartwarren.logit.zmq.ZmqTransport;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 * 
 * Log4j1 ZMQ Appender
 */
public class ZmqAppender extends AccessLogValve implements IZmqTransport, IFrameworkLayout {
    
    private transient final ZmqTransport appender = new ZmqTransport();
    private transient Layout layout;
    private static final String DEFAULT_LAYOUT = "com.stuartwarren.logit.tomcatvalve.Layout";
    
    public ZmqAppender() {
        super();
        LogitLog.debug("TomcatValve ZMQ appender in use.");
    }
    
    /**
     * @see org.apache.catalina.valves.ValveBase#getInfo()
     */
    public String getInfo() {
        return getClass() + "/1.0";
    }
    
    @Override
    public void log(final Request request, final Response response, final long time) {
        if (!this.appender.isConfigured()) {
            this.appender.configure();
        }
        LogitLog.trace("About to append log.");
        append(request, response, time);
    }
    
    /**
     * Stop this component and implement the requirements
     * of {@link org.apache.catalina.util.LifecycleBase#stopInternal()}.
     *
     * @exception LifecycleException if this component detects a fatal error
     *  that prevents this component from being used
     */
    @Override
    protected synchronized void stopInternal() throws LifecycleException {
        setState(LifecycleState.STOPPING);
        close();
    }


    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#close()
     */
    public void close() {
        this.appender.stop();
        LogitLog.debug("ZMQ context should now be closed!");
    }
    
    public void activateOptions() {
        //this.appender.configure();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#requiresLayout()
     */
    public boolean requiresLayout() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    protected void append(final Request request, final Response response, final long time) {
        final String log = this.layout.format(request, response, time);
        LogitLog.debug("Appending.");
        this.appender.appendString(log);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#getEndpoints()
     */
    public String getEndpoints() {
        return this.appender.getEndpoints();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#setEndpoints(java.lang.String)
     */
    public void setEndpoints(final String endpoints) {
        LogitLog.debug("setEndpoints.");
        this.appender.setEndpoints(endpoints);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#getSocketType()
     */
    public String getSocketType() {
        return this.appender.getSocketType();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#setSocketType(java.lang.String)
     */
    public void setSocketType(final String socketType) {
        LogitLog.debug("setSocketType.");
        this.appender.setSocketType(socketType);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#getLinger()
     */
    public int getLinger() {
        return this.appender.getLinger();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#setLinger(int)
     */
    public void setLinger(final String linger) {
        LogitLog.debug("setLinger.");
        this.appender.setLinger(Integer.parseInt(linger));
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#getBindConnect()
     */
    public String getBindConnect() {
        return this.appender.getBindConnect();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#setBindConnect(java.lang.String)
     */
    public void setBindConnect(final String bindConnect) {
        LogitLog.debug("setBindConnect.");
        this.appender.setBindConnect(bindConnect);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#getSendHWM()
     */
    public int getSendHWM() {
        return this.appender.getSendHWM();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.ITransport#setSendHWM(int)
     */
    public void setSendHWM(final String sendHWM) {
        LogitLog.debug("setSendHWM.");
        this.appender.setSendHWM(Integer.parseInt(sendHWM));
    }
    
    /**
     * 
     * @param layout
     *          Name of class to use to format the logs.
     */
    public void setLayout(final String layout) {
        LogitLog.debug("setLayout.");
        if ( null != layout) {
            final Layout l = (Layout) LayoutLoader.instantiateByClassName(layout, DEFAULT_LAYOUT);
            if (LogitLog.isDebugEnabled()) {
                LogitLog.debug("Loading layout class: " + layout);
            }
            this.layout = l;
        }
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#getLayoutType()
     */
    public String getLayoutType() {
        return this.layout.getLayoutType();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#setLayoutType(java.lang.String)
     */
    public void setLayoutType(final String layoutType) {
        LogitLog.debug("setLayoutType.");
        this.layout.setLayoutType(layoutType);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#getDetailThreshold()
     */
    public String getDetailThreshold() {
        return this.layout.getDetailThreshold();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#setDetailThreshold(java.lang.String)
     */
    public void setDetailThreshold(final String detailThreshold) {
        LogitLog.debug("setDetailThreshold.");
        this.layout.setDetailThreshold(detailThreshold);        
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#getFields()
     */
    public String getFields() {
        return this.layout.getFields();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#setFields(java.lang.String)
     */
    public void setFields(final String fields) {
        LogitLog.debug("setFields.");
        this.layout.setFields(fields);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#getTags()
     */
    public String getTags() {
        return this.layout.getTags();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.IFrameworkLayout#setTags(java.lang.String)
     */
    public void setTags(final String tags) {
        LogitLog.debug("setTags.");
        this.layout.setTags(tags);   
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.zmq.IZmqTransport#setLinger(int)
     */
    public void setLinger(final int linger) {
        LogitLog.debug("Calling unexpected method signature: linger");
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.zmq.IZmqTransport#setSendHWM(int)
     */
    public void setSendHWM(final int sendHWM) {
        LogitLog.debug("Calling unexpected method signature: sendHWM");
    }
    
    public void setIHeaders(final String headers) {
        this.layout.setIHeaders(headers);
    }
    
    public void setOHeaders(final String headers) {
        this.layout.setOHeaders(headers);
    }
    
    public void setCookies(final String cookies) {
        this.layout.setCookies(cookies);
    }

}
