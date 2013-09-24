package com.stuartwarren.logit;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class LogIt {
	
	static Logger logger = Logger.getLogger(LogIt.class.getName());
	
	public static void main(String[] args) throws InterruptedException
	{
		Map<String,Object> metricMap = new HashMap<String,Object>();
		metricMap.put("com.website.www.500Errors", 1);
		MDC.put("metrics", metricMap);
		MDC.put("tags", "other");
		logger.debug("Hello World!");
		MDC.clear();
		logger.debug("Hi there");
	}

}
