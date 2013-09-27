package com.stuartwarren.test_logit.log4j1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		List<String> tags = new ArrayList<String>();
		tags.add("otherTag");
		MDC.put("tags", tags);
		logger.debug("Hello World!");
		MDC.clear();
		logger.info("Hi there");
		logger.warn("Hi there");
		logger.error("Hi there");
		logger.fatal("Hi there");
	}

}
