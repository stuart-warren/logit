/**
 * 
 */
package com.stuartwarren.logit.log4j1.zmq;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.jeromq.ZMQ;

/**
 * @author Stuart Warren 
 * @date 26 Sep 2013
 *
 */
public class ZmqAppender extends AppenderSkeleton {

	private ZMQ.Socket socket;
	private String destination = "tcp://localhost:2120";
	
	public ZmqAppender() {
		super();
	}

	public ZmqAppender(final ZMQ.Socket socket) {
		this();
		this.socket = socket;
	}
	
	public void activateOptions() {
		final ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket sender;
		sender = context.socket(ZMQ.PUSH);
		sender.setLinger(2);
		final ZMQ.Socket socket = sender;
		socket.connect(destination);
		this.socket = socket;
	}
	
	public void close() {
		System.out.println("asked to close!");
		if(this.closed) // closed is defined in AppenderSkeleton
		    return;
		this.closed = true;
	}

	public boolean requiresLayout() {
		return true;
	}

	public void append(LoggingEvent event) {
		String log = this.layout.format(event);
		// remove new line character from end
		log = log.substring(0, log.length() - 1);
		socket.send(log);
	}

}
