/**
 * 
 */
package com.stuartwarren.logit.log4j1.zmq;

import java.io.InterruptedIOException;
import java.net.InetAddress;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;


/**
 * @author Stuart Warren 
 * @date 25 Sep 2013
 *
 */
public class PushAppender extends AppenderSkeleton {
	
	static public final int DEFAULT_PORT = 2120;
	static final int DEFAULT_RECONNECTION_DELAY = 20000;
	String remoteHost;
	InetAddress address;
	int port = DEFAULT_PORT;
	int reconnectionDelay = DEFAULT_RECONNECTION_DELAY;
	boolean locationInfo = false;
	
	public PushAppender() {
		
	}
	
	public PushAppender(InetAddress address, int port){
		this.address = address;
		this.remoteHost = address.getHostName();
		this.port = port;
		connect(address, port);
	}

	public PushAppender(String host, int port){
		this.port = port;
		this.address = getAddressByName(host);
		this.remoteHost = host;
		connect(address, port);
	}
	
	/**
	 * @param address
	 * @param port
	 */
	private void connect(InetAddress address, int port) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#close()
	 */
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append(LoggingEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	static InetAddress getAddressByName(String host) {
		try {
			return InetAddress.getByName(host);
	    } catch(Exception e) {
	    	if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
	    		Thread.currentThread().interrupt();
	    	}
	    	LogLog.error("Could not find address of ["+host+"].", e);
	    	return null;
	    }
	}



}
