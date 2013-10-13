/**
 * 
 */
package com.stuartwarren.logit.log4j1;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.stuartwarren.logit.utils.LogitLog;
import com.stuartwarren.logit.zmq.IZmqTransport;
import com.stuartwarren.logit.zmq.ZmqTransport;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 * 
 * Log4j1 ZMQ Appender
 */
public class ZmqAppender extends AppenderSkeleton implements IZmqTransport {
    
    private ZmqTransport appender;
    
    public ZmqAppender() {
        LogitLog.debug("Log4j1 ZMQ appender in use.");
        ShutdownHook sh = new ShutdownHook();
        sh.attachShutDownHook();
        this.appender = new ZmqTransport();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#close()
     */
    public void close() {
        if (this.closed) { // closed is defined in AppenderSkeleton
            LogitLog.debug("ZMQ context is already closed!");
            return;
        }
        this.appender.stop();
        this.closed = true;
        LogitLog.debug("ZMQ context should now be closed!");
    }
    
    public void activateOptions() {
        this.appender.configure();
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
    protected void append(LoggingEvent event) {
        String log = this.layout.format(event);
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
    public void setEndpoints(String endpoints) {
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
    public void setSocketType(String socketType) {
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
    public void setLinger(int linger) {
        this.appender.setLinger(linger);
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
    public void setBindConnect(String bindConnect) {
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
    public void setSendHWM(int sendHWM) {
        this.appender.setSendHWM(sendHWM);
    }

}
