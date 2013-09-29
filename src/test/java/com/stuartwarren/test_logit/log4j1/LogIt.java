package com.stuartwarren.test_logit.log4j1;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogIt {
	
	public static void main(String[] args)
	{
		System.setProperty("log4j.debug", "");
		Logger logger = Logger.getLogger(LogIt.class.getName());
		System.out.println("First instruction of Program....");
		int n = 0;
		while (n < 1000000) {
			n = n + 1;
			logger.info("Hi there " + n);
		}
		System.out.println("Last instruction of Program....");
		
		/* 
		 * Need to exit the application or shutdown the log manager
		 * to close ZMQ sockets cleanly.
		 */
		LogManager.shutdown();
		//System.exit(0);
	}

}
