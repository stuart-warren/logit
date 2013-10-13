/**
 * 
 */
package com.stuartwarren.logit.logback;

import ch.qos.logback.core.OutputStreamAppender;

import com.stuartwarren.logit.utils.LogitLog;
import com.stuartwarren.logit.zmq.IZmqTransport;
import com.stuartwarren.logit.zmq.ZmqOutputStream;
import com.stuartwarren.logit.zmq.ZmqTransport;

/**
 * @author Stuart Warren 
 * @param <E>
 * @date 6 Oct 2013
 * 
 * Log4j1 ZMQ Appender
 */
public class ZmqAppender<E> extends OutputStreamAppender<E> implements IZmqTransport {
    
    private ZmqTransport appender;
    private ZmqOutputStream outputStream;
    
    public ZmqAppender() {
        LogitLog.debug("Logback ZMQ appender in use.");
        ShutdownHook sh = new ShutdownHook();
        sh.attachShutDownHook();
        this.appender = new ZmqTransport();
    }
    
    public void start() {
        this.appender.configure();
        // Set the socket as an OutputStream
        outputStream = new ZmqOutputStream(this.appender.getSocket());
        setOutputStream(outputStream);
        super.start();
    }

    @Override
    public void stop() {
        this.appender.stop();
        LogitLog.debug("ZMQ context should now be closed!");
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
