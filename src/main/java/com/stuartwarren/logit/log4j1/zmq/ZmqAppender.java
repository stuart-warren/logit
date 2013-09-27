/**
 * 
 */
package com.stuartwarren.logit.log4j1.zmq;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.jeromq.ZMQ;

import com.stuartwarren.logit.log4j1.logstash.LogstashV1Layout;

/**
 * @author Stuart Warren 
 * @date 26 Sep 2013
 *
 */
public class ZmqAppender extends AppenderSkeleton {

	private ZMQ.Socket socket;
	
	private int threads;
	private int hwm;
	private String endpoint;
	private String mode;
	private int socketType = ZMQ.PUSH;
	private String topic;
	private String identity;
	private boolean blocking;
	private String destination = "tcp://localhost:2120";
	
	//protected Layout layout = new LogstashV1Layout();
	
	public ZmqAppender() {
		super();
	}
	
	public void activateOptions() {
		super.activateOptions();
		try {
			final ZMQ.Context context = ZMQ.context(1);
			ZMQ.Socket sender;
			sender = context.socket(socketType);
			sender.setLinger(2);
			sender.connect(destination);
			this.socket = sender;
		} catch (Exception e) {
			LogLog.error("Issue connecting to destination", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#close()
	 */
	public void close() {
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			LogLog.error("Could not sleep required time to comlete send. May have lost messages in buffers.", e);
//		}
//		sender.close();
//        context.term();
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
	@Override
	protected void append(LoggingEvent event) {
		socket.send(layout.format(event));
	}

}
