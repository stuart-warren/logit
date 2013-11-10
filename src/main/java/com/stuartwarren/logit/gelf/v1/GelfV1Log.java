/**
 * 
 */
package com.stuartwarren.logit.gelf.v1;

import java.io.IOException;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public final class GelfV1Log extends Log {

    private String                  version;
    private final transient Map<String, Object> jacksonOutput;
    private final static String ID_STRING = "id";

    public GelfV1Log() {
        super();
        this.jacksonOutput = new HashMap<String, Object>();
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
    
    private void addEventData(final String key, final Object val) {
        addEventData(key, val, false);
    }

    @SuppressWarnings("unchecked")
    private void addEventData(final String key, final Object val, final boolean builtIn) {
        if (val instanceof HashMap && !((HashMap<String, Object>) val).isEmpty() && !ID_STRING.equals(key)) {
            jacksonOutput.put("_" + key, val);
        } else if (null != val && !ID_STRING.equals(key) && builtIn){
                    jacksonOutput.put(key, val);
                } else {
                    jacksonOutput.put('_' + key, val);
        }
    }

    public String toString() {
        addEventData("facility", this.getLoggerName(), true);
        addEventData("level", this.getLevelInt(), true);
        addEventData("host", this.getHostname(), true);
        addEventData("user", this.getUsername());
        addEventData("ndc", this.getNdc());
        addEventData("tags", this.getTags());
        addEventData("thread", this.getThreadName());
        final Map<String, Object> fields = this.getFields();
        if (null != fields) {
            for (final Map.Entry<String, Object> entry : fields.entrySet()) {
                addEventData(entry.getKey(), entry.getValue());
            }
        }
        addEventData("mdc", this.getMdc());
        addEventData("version", this.getVersion(), true);
        addEventData("timestamp", String.valueOf(this.getTimestamp()), true);
        addEventData("short_message", this.getMessage(), true);
        String log;
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
