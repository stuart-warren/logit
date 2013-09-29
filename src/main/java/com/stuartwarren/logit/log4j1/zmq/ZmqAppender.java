/**
 * 
 */
package com.stuartwarren.logit.log4j1.zmq;


import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.jeromq.ZMQ;

import com.stuartwarren.logit.log4j1.ShutdownHook;

/**
 * @author Stuart Warren 
 * @date 26 Sep 2013
 *
 */
public class ZmqAppender extends AppenderSkeleton {

	final ZMQ.Context context = ZMQ.context(1);
	private ZMQ.Socket socket;
	private String destination = "tcp://localhost:2120";
	
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
		sender = context.socket(ZMQ.PUSH);
		sender.setLinger(0);
		final ZMQ.Socket socket = sender;
		socket.connect(destination);
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

}
