/**
 * 
 */
package com.stuartwarren.logit.logstash.v1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.JsonGenerationException;
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
    private transient final Map<IFieldName, Object> jacksonOutput = new ConcurrentHashMap<IFieldName, Object>();

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
    public void setTimestamp(final long timestamp) {
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
    public void setVersion(final String version) {
        this.version = version;
    }

    @SuppressWarnings("unchecked")
    private void addEventData(final IFieldName key, final Object val) {
        if (val instanceof HashMap && !((HashMap<String, Object>) val).isEmpty()) {
            jacksonOutput.put(key, val);

        } else if (null != val) {
            jacksonOutput.put(key, val);
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
        addEventData(ROOT.LOGIT, this.getLogitVersion());
        final Map<IFieldName, Object> fields = this.getFields();
        if (null != fields) {
            for (final Map.Entry<IFieldName, Object> entry : fields.entrySet()) {
                addEventData(entry.getKey(), entry.getValue());
            }
        }
        addEventData(ROOT.MDC, this.getMdc());
        addEventData(LOGSTASH.VERSION, this.getVersion());
        addEventData(LOGSTASH.TIMESTAMP, new LogstashTimestamp(this.getTimestamp()).toString());
        addEventData(ROOT.MESSAGE, this.getMessage());
        try {
            final ObjectMapper mapper = new ObjectMapper();
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
