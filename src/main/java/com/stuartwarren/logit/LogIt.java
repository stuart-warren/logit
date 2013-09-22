package com.stuartwarren.logit;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class LogIt {
	
	static Logger logger = Logger.getLogger(LogIt.class.getName());
	
	public static void main(String[] args) throws InterruptedException
	{
		MDC.put("test", 2);
		MDC.put("tags", "other");
		logger.debug("Hello World!");
		logger.debug("Hi there");
	}

}
