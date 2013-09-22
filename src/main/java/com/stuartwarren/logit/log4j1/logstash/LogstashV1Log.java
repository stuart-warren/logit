/**
 * 
 */
package com.stuartwarren.logit.log4j1.logstash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.stuartwarren.logit.CommonLog;

/**
 * @author Stuart Warren 
 * @date 22 Sep 2013
 *
 */
public class LogstashV1Log extends CommonLog {
	
	private LogstashTimestamp timestamp;
	private String version;
	private ArrayList<String> tags;
	private HashMap<String,Object> jacksonOutput;
	
	private ObjectMapper mapper;
	
	public LogstashV1Log() {
		super();
		this.mapper = new ObjectMapper();
		this.jacksonOutput = new HashMap<String,Object>();
	}
	
	/**
	 * @return the timestamp
	 */
	public LogstashTimestamp getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = new LogstashTimestamp(timestamp);
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the tags
	 */
	public ArrayList<String> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	private void addEventData(String key, Object val) {
		if (val instanceof HashMap) {
			if (!((HashMap<String, Object>) val).isEmpty()) {
				jacksonOutput.put(key, val);
			}
		} else if (null != val) {
			jacksonOutput.put(key, val);
		}
	}
	
	public String toString() {
		String log;
		//addEventData("mdc", this.getMdc());
		Map<String,Object> mdc = this.getMdc();
		for (Map.Entry<String, Object> entry : mdc.entrySet()){
			addEventData(entry.getKey(), entry.getValue());
		}
		addEventData("@version", this.getVersion());
		addEventData("@timestamp", this.getTimestamp().toString());
		addEventData("message", this.getMessage());
		addEventData("ndc", this.getNdc());
		addEventData("tags", this.getTags());
		addEventData("thread", this.getThreadName());
		addEventData("exception", this.getExceptionInformation());
		addEventData("location", this.getLocationInformation());
		addEventData("logger", this.getLoggerName());
		addEventData("level", this.getLevel());
		try {
			log = mapper.writeValueAsString(jacksonOutput);
		} catch (JsonGenerationException e) {
			log = e.toString();
		} catch (JsonMappingException e) {
			log = e.toString();
		} catch (IOException e) {
			log = e.toString();
		}
		return log + "\n";
	}

}
