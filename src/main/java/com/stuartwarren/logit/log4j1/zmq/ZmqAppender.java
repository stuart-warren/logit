/**
 * 
 */
package com.stuartwarren.logit.log4j1.zmq;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.jeromq.ZMQ;

import com.stuartwarren.logit.log4j1.ShutdownHook;
import com.stuartwarren.logit.zmq.SocketType;

/**
 * @author Stuart Warren 
 * @date 26 Sep 2013
 *
 */
public class ZmqAppender extends AppenderSkeleton {

	final ZMQ.Context context = ZMQ.context(1);
	private ZMQ.Socket socket;

	private static final String CONNECTMODE = "connect";
	private static final String BINDMODE = "bind";
	
	// Set some defaults
	private String endpoints = "tcp://localhost:2120";
	private String socketType = SocketType.PUSHPULL.toString();
	private String bindConnect = CONNECTMODE;
	private int linger = 1;
	
	public ZmqAppender() {
		super();
		LogLog.debug("Starting ZmqAppender");
		ShutdownHook sh = new ShutdownHook();
		sh.attachShutDownHook();
	}

	public ZmqAppender(final ZMQ.Socket socket) {
		this();
		this.socket = socket;
	}
	
	public void activateOptions() {  
		ZMQ.Socket sender;
		sender = context.socket(SocketType.getServerSocket(socketType));
		LogLog.debug("SocketType = "+ socketType);
		sender.setLinger(linger);
		LogLog.debug("Linger = "+ linger);
		final ZMQ.Socket socket = sender;
		final String[] endpointsList = endpoints.split(",");
		LogLog.debug("Endpoints = "+ endpoints);
		for(String endpoint : endpointsList) {
			if (CONNECTMODE.equalsIgnoreCase(bindConnect)) {
				LogLog.debug("Connecting to "+ endpoint);
				socket.connect(endpoint);
			} else if (BINDMODE.equalsIgnoreCase(bindConnect)) {
				LogLog.debug("Binding to "+ endpoint);
				socket.bind(endpoint);
			} else {
				LogLog.error(" to "+ endpoint);
				socket.connect(endpoint);
			}
		}
		this.socket = socket;
	}
	
	public void close() {
		LogLog.debug("Asked to close ZMQ context!");
		if(this.closed) { // closed is defined in AppenderSkeleton
			LogLog.debug("ZMQ context is already closed!");
		    return;
		}
		socket.close();
		context.term();
		socket = null;
		this.closed = true;
		LogLog.debug("ZMQ context should be closed");
	}

	public boolean requiresLayout() {
		return true;
	}

	public void append(LoggingEvent event) {
		String log = this.layout.format(event);
		// remove new line character from end
		log = log.substring(0, log.length() - 1);
		socket.send(log,ZMQ.NOBLOCK);
	}

	/**
	 * @return the endpoints
	 */
	public String getEndpoints() {
		return endpoints;
	}

	/**
	 * @param endpoints the endpoints to set
	 */
	public void setEndpoints(String endpoints) {
		this.endpoints = endpoints;
	}
	
	/**
	 * @return the socketType
	 */
	public String getSocketType() {
		return socketType;
	}

	/**
	 * @param socketType the socketType to set
	 * @throws ParserConfigurationException 
	 */
	public void setSocketType(String socketType) throws ParserConfigurationException {
		socketType = socketType.toLowerCase();
		if (SocketType.isValidType(socketType)) {
			this.socketType = socketType;
		}else{
			 throw new ParserConfigurationException("Invalid SocketType chosen. Using default.");
		}
	}

	/**
	 * @return the linger
	 */
	public int getLinger() {
		return linger;
	}

	/**
	 * @param linger the linger to set
	 */
	public void setLinger(int linger) {
		this.linger = linger;
	}

	/**
	 * @return the bindConnect
	 */
	public String getBindConnect() {
		return bindConnect;
	}

	/**
	 * @param bindConnect the bindConnect to set
	 * @throws ParserConfigurationException 
	 */
	public void setBindConnect(String bindConnect) throws ParserConfigurationException {
		bindConnect = bindConnect.toLowerCase();
		if ((bindConnect.equals(BINDMODE))||(bindConnect.equals(CONNECTMODE))) {
			this.bindConnect = bindConnect;
		} else {
			throw new ParserConfigurationException("Invalid BindConnect chosen. Using default.");
		}
	}

}
