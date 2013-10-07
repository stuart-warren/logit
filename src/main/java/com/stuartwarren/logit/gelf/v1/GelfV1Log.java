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
    private HashMap<String, Object> jacksonOutput;

    private ObjectMapper            mapper;

    public GelfV1Log() {
        super();
        this.mapper = new ObjectMapper();
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
    public void setVersion(String version) {
        this.version = version;
    }
    
    private void addEventData(String key, Object val) {
        addEventData(key, val, false);
    }

    @SuppressWarnings("unchecked")
    private void addEventData(String key, Object val, boolean builtIn) {
        if (val instanceof HashMap) {
            if (!((HashMap<String, Object>) val).isEmpty()) {
                if (!"id".equals(key))
                    jacksonOutput.put("_" + key, val);
            }
        } else if (null != val) {
            if (!"id".equals(key)) {
                if (builtIn) {
                    jacksonOutput.put(key, val);
                } else {
                    jacksonOutput.put('_' + key, val);
                }
            }
        }
    }

    public String toString() {
        Map<String, Object> fields = this.getFields();
        if (null != fields) {
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                addEventData(entry.getKey(), entry.getValue());
            }
        }
        Map<String, Object> mdc = this.getMdc();
        for (Map.Entry<String, Object> entry : mdc.entrySet()) {
            addEventData(entry.getKey(), entry.getValue());
        }
        addEventData("version", this.getVersion(), true);
        addEventData("timestamp", String.valueOf(this.getTimestamp()), true);
        addEventData("short_message", this.getMessage(), true);
        addEventData("facility", this.getLoggerName(), true);
        addEventData("level", this.getLevel_int(), true);
        try {
            addEventData("full_message", this.getExceptionInformation().toString(), true);
        } catch (NullPointerException e) {}
        addEventData("host", null, true);
        addEventData("ndc", this.getNdc());
        addEventData("tags", this.getTags());
        addEventData("thread", this.getThreadName());
        try {
            addEventData("location", this.getLocationInformation().toString());
            addEventData("file", this.getLocationInformation().getFileName(), true);
            addEventData("line", this.getLocationInformation().getLineNumber(), true);
        } catch (NullPointerException e) {}
        String log;
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
