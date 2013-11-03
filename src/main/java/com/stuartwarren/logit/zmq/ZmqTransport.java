/**
 * 
 */
package com.stuartwarren.logit.zmq;

import org.jeromq.ZMQ;

import zmq.ZError.IOException;

import com.stuartwarren.logit.appender.IAppender;
import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class ZmqTransport implements IAppender, IZmqTransport {
    
    final   ZMQ.Context         context     = ZMQ.context(1);
    private ZMQ.Socket          socket;

    private static final String CONNECTMODE = "connect";
    private static final String BINDMODE    = "bind";
    
    // Set some defaults
    private String              endpoints   = "tcp://localhost:2120";
    private String              socketType  = SocketType.PUSHPULL.toString();
    private String              bindConnect = CONNECTMODE;
    private int                 sendHWM     = 1;
    private int                 linger      = 1;
    private boolean             configured  = false;
    
    /**
     * 
     */
    public ZmqTransport() {
        LogitLog.debug("ZMQTransport in use.");
    }
    
    public ZmqTransport(final ZMQ.Socket socket) {
        this();
        this.socket = socket;
    }
    
    public ZMQ.Socket getSocket(){
        return this.socket;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#configure()
     */
    public void configure() {
        ZMQ.Socket sender;
        LogitLog.debug("Setting property [socketType] to [" + socketType + "].");
        sender = context.socket(SocketType.getServerSocket(socketType));
        //sender = context.createSocket(SocketType.getClientSocket(socketType));
        LogitLog.debug("Setting property [linger] to [" + linger + "].");
        sender.setLinger(linger);
        LogitLog.debug("Setting property [sendHWM] to [" + sendHWM + "].");
        sender.setSndHWM(sendHWM);
        final ZMQ.Socket socket = sender;
        LogitLog.debug("Setting property [endpoints] to [" + endpoints + "].");
        final String[] endpointsList = endpoints.split(",");
        for (String endpoint : endpointsList) {
            LogitLog.debug("Setting property [bindConnect] to [" + bindConnect + "].");
            if (CONNECTMODE.equalsIgnoreCase(bindConnect)) {
                socket.connect(endpoint);
            } else if (BINDMODE.equalsIgnoreCase(bindConnect)) {
                socket.bind(endpoint);
            } else {
                socket.connect(endpoint);
            }
        }
        this.socket = socket;
        LogitLog.debug("ZMQTransport configured.");
        this.configured = true;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#stop()
     */
    public synchronized void stop() {
        if (null != socket) {
            LogitLog.debug("Closing socket.");
            socket.close();
            context.term();
            //zcontext.destroy();
            socket = null;
        } else {
            return;
        }
        LogitLog.debug("Socket should be closed.");
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#append(java.lang.String)
     */
    public void appendString(String log) {
        log = log.substring(0, log.length() - 1);
        LogitLog.debug("Sending log: [" + log + "].");
        try {
            socket.send(log, ZMQ.NOBLOCK);
            // Has occasionally been known to throw a java.nio.channels.ClosedByInterruptException
        } catch (IOException e) {
            // Try again after sleeping for a second
            try {
                Thread.sleep(1000);
                socket.send(log, ZMQ.NOBLOCK);
            } catch (InterruptedException i) {
                // do nothing
            } catch (IOException e2) {
                LogitLog.error("Could not send following log on the second attempt: [" + log + "].", e2);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getEndpoints()
     */
    public String getEndpoints() {
        return this.endpoints;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setEndpoints(java.lang.String)
     */
    public void setEndpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getSocketType()
     */
    public String getSocketType() {
        return this.socketType;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setSocketType(java.lang.String)
     */
    public void setSocketType(String socketType) {
        if (SocketType.isValidType(socketType)) {
            this.socketType = socketType;
        }
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getLinger()
     */
    public int getLinger() {
        return this.linger;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setLinger(int)
     */
    public void setLinger(int linger) {
        this.linger = linger;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getBindConnect()
     */
    public String getBindConnect() {
        return this.bindConnect;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setBindConnect(java.lang.String)
     */
    public void setBindConnect(String bindConnect) {
        if ((bindConnect.equals(BINDMODE)) || (bindConnect.equals(CONNECTMODE))) {
            this.bindConnect = bindConnect;
        }
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getSendHWM()
     */
    public int getSendHWM() {
        return this.sendHWM;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setSendHWM(int)
     */
    public void setSendHWM(int sendHWM) {
        this.sendHWM = sendHWM;
    }
    
    public boolean isConfigured() {
        return this.configured;
    }


}
