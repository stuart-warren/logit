/**
 * 
 */
package com.stuartwarren.logit.logstash.v1;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.fields.IFieldName;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.logstash.LogstashField.LOGSTASH;
import com.stuartwarren.logit.logstash.LogstashTimestamp;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public final class LogstashV1Log extends Log {

    private long                    timestamp;
    private String                  version;
    private HashMap<String, Object> jacksonOutput;

    private ObjectMapper            mapper;

    public LogstashV1Log() {
        super();
        this.mapper = new ObjectMapper();
        this.jacksonOutput = new HashMap<String, Object>();
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
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

    // TODO: Make me accept enum keys only
    @SuppressWarnings("unchecked")
    private void addEventData(IFieldName key, Object val) {
        if (val instanceof HashMap) {
            if (!((HashMap<String, Object>) val).isEmpty()) {
                jacksonOutput.put(key.toString(), val);
            }
        } else if (null != val) {
            jacksonOutput.put(key.toString(), val);
        }
    }

    // TODO: Maybe try JsonNodeFactory
    public String toString() {
        String log;
        addEventData(ROOT.NDC, this.getNdc());
        addEventData(ROOT.TAGS, this.getTags());
        addEventData(ROOT.THREAD, this.getThreadName());
        addEventData(ROOT.LOGGER, this.getLoggerName());
        addEventData(ROOT.LEVEL, this.getLevel());
        addEventData(ROOT.USER, this.getUsername());
        addEventData(ROOT.HOSTNAME, this.getHostname());
        Map<String, Object> fields = this.getFields();
        if (null != fields) {
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                addEventData(entry.getKey(), entry.getValue());
            }
        }
        addEventData(ROOT.MDC, this.getMdc());
        addEventData(LOGSTASH.VERSION, this.getVersion());
        addEventData(LOGSTASH.TIMESTAMP, new LogstashTimestamp(this.getTimestamp()).toString());
        addEventData(ROOT.MESSAGE, this.getMessage());
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
