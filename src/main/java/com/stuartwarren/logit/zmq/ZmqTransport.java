/**
 * 
 */
package com.stuartwarren.logit.zmq;

import org.jeromq.ZMQ;

import com.stuartwarren.logit.appender.IAppender;

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
    
    /**
     * 
     */
    public ZmqTransport() {
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
        sender = context.socket(SocketType.getServerSocket(socketType));
        //sender = context.createSocket(SocketType.getClientSocket(socketType));
        sender.setLinger(linger);
        sender.setSndHWM(sendHWM);
        final ZMQ.Socket socket = sender;
        final String[] endpointsList = endpoints.split(",");
        for (String endpoint : endpointsList) {
            if (CONNECTMODE.equalsIgnoreCase(bindConnect)) {
                socket.connect(endpoint);
            } else if (BINDMODE.equalsIgnoreCase(bindConnect)) {
                socket.bind(endpoint);
            } else {
                socket.connect(endpoint);
            }
        }
        this.socket = socket;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#stop()
     */
    public void stop() {
        socket.close();
        context.term();
        //zcontext.destroy();
        socket = null;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.ITransport#append(java.lang.String)
     */
    public void appendString(String log) {
        log = log.substring(0, log.length() - 1);
        socket.send(log, ZMQ.NOBLOCK);
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getEndpoints()
     */
    @Override
    public String getEndpoints() {
        return this.endpoints;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setEndpoints(java.lang.String)
     */
    @Override
    public void setEndpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getSocketType()
     */
    @Override
    public String getSocketType() {
        return this.socketType;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setSocketType(java.lang.String)
     */
    @Override
    public void setSocketType(String socketType) {
        if (SocketType.isValidType(socketType)) {
            this.socketType = socketType;
        }
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getLinger()
     */
    @Override
    public int getLinger() {
        return this.linger;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setLinger(int)
     */
    @Override
    public void setLinger(int linger) {
        this.linger = linger;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getBindConnect()
     */
    @Override
    public String getBindConnect() {
        return this.bindConnect;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setBindConnect(java.lang.String)
     */
    @Override
    public void setBindConnect(String bindConnect) {
        if ((bindConnect.equals(BINDMODE)) || (bindConnect.equals(CONNECTMODE))) {
            this.bindConnect = bindConnect;
        }
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#getSendHWM()
     */
    @Override
    public int getSendHWM() {
        return this.sendHWM;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.appender.IAppender#setSendHWM(int)
     */
    @Override
    public void setSendHWM(int sendHWM) {
        this.sendHWM = sendHWM;
    }


}
