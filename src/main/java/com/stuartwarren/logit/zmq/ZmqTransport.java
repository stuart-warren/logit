/**
 * 
 */
package com.stuartwarren.logit.zmq;

import org.zeromq.ZMQ;

import zmq.ZError.IOException;

import com.stuartwarren.logit.appender.IAppender;
import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class ZmqTransport implements IAppender, IZmqTransport {
    
    private final transient   ZMQ.Context         context     = ZMQ.context(1);
    private transient         ZMQ.Socket          socket;

    private static final String CONNECTMODE = "connect";
    private static final String BINDMODE    = "bind";
    
    // Set some defaults
    private String              endpoints   = "tcp://localhost:2120";
    private String              socketType  = SocketType.PUSHPULL.toString();
    private String              bindConnect = CONNECTMODE;
    private int                 sendHWM     = 1;
    private int                 linger      = 1;
    private boolean             configured;
    
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
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [socketType] to [" + socketType + "].");
        }
        sender = context.socket(SocketType.getServerSocket(socketType));
        //sender = context.createSocket(SocketType.getClientSocket(socketType));
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [linger] to [" + linger + "].");
        }
        sender.setLinger(linger);
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [sendHWM] to [" + sendHWM + "].");
        }
        sender.setSndHWM(sendHWM);
        final ZMQ.Socket socket = sender;
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [endpoints] to [" + endpoints + "].");
        }
        final String[] endpointsList = endpoints.split(",");
        for (String endpoint : endpointsList) {
            if (LogitLog.isDebugEnabled()) {
                LogitLog.debug("Setting property [bindConnect] to [" + bindConnect + "].");
            }
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
        this.setConfigured(true);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#stop()
     */
    public synchronized void stop() {
        if (null == socket) {
            return;
        } else {
            LogitLog.debug("Closing socket.");
            socket.close();
            context.term();
            setConfigured(false);
            //zcontext.destroy();
            socket = null; // NOPMD by stuart on 10/11/13 20:11
        }
        LogitLog.debug("Socket should be closed.");
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#append(java.lang.String)
     */
    public void appendString(final String line) {
        final String log = line.substring(0, line.length() - 1);
        if (LogitLog.isTraceEnabled()) {
            LogitLog.trace("Sending log: [" + log + "].");
        }
        try {
            socket.send(log, ZMQ.NOBLOCK);
            // Has occasionally been known to throw a java.nio.channels.ClosedByInterruptException
        } catch (IOException e) {
            LogitLog.warn("IOException thrown, will try sending log again shortly after recreating the connection", e);
            // Try again after sleeping for a second
            try {
                // TODO: See if this gets around issues with threads being killed?
                // kill the socket
                socket.close();
                this.setConfigured(true);
                // Recreate it
                configure();                
                socket.send(log, ZMQ.NOBLOCK);
            } catch (IOException e2) {
                LogitLog.error("Could not send following log on the second attempt: [" + log + "].", e2);
            }
        } catch (Exception g) {
            LogitLog.error("Something threw an exception that wasn't IOException.", g);
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
    public void setEndpoints(final String endpoints) {
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
    public void setSocketType(final String socketType) {
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
    public void setLinger(final int linger) {
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
    public void setBindConnect(final String bindConnect) {
        if (bindConnect.equals(BINDMODE) || bindConnect.equals(CONNECTMODE)) {
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
    public void setSendHWM(final int sendHWM) {
        this.sendHWM = sendHWM;
    }
    
    public boolean isConfigured() {
        return this.configured;
    }

    /**
     * @param configured the configured to set
     */
    public void setConfigured(final boolean configured) {
        this.configured = configured;
    }


}
