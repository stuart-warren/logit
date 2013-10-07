/**
 * 
 */
package com.stuartwarren.logit.logstash.v0;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.logstash.LogstashTimestamp;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public final class LogstashV0Log extends Log {

    private long                    timestamp;
    private String                  version;
    private HashMap<String, Object> jacksonOutput;

    private ObjectMapper            mapper;

    public LogstashV0Log() {
        super();
        this.mapper = new ObjectMapper();
        this.jacksonOutput = new HashMap<String, Object>();
    }

    /**
     * @return the timestamp
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @SuppressWarnings("unchecked")
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
        addEventData("@version", this.getVersion());
        addEventData("@timestamp", new LogstashTimestamp(this.getTimestamp()).toString());
        addEventData("@message", this.getMessage());
        addEventData("@tags", this.getTags());
        addField("mdc", this.getMdc());
        addField("ndc", this.getNdc());
        addField("thread", this.getThreadName());
        addField("exception", this.getExceptionInformation());
        addField("location", this.getLocationInformation());
        addField("logger", this.getLoggerName());
        addField("level", this.getLevel());
        addEventData("@fields", this.getFields());
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
