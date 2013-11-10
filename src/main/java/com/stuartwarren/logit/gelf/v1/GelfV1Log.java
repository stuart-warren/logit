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

import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.fields.IFieldName;
import com.stuartwarren.logit.gelf.v1.GelfV1Field.GELF;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 * TODO: This needs work!
 */
public final class GelfV1Log extends Log {

    private String                  version;
    private final transient Map<String, Object> jacksonOutput = new HashMap<String, Object>();
    private final static String ID_STRING = "id";

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
    
    private void addEventData(final IFieldName key, final Object val) {
        addEventData(key, val, false);
    }

    @SuppressWarnings("unchecked")
    private void addEventData(final IFieldName key, final Object val, final boolean builtIn) {
        if (val instanceof HashMap && !((HashMap<String, Object>) val).isEmpty() && !ID_STRING.equals(key)) {
            jacksonOutput.put("_" + key, val);
        } else if (null != val && !ID_STRING.equals(key) && builtIn){
                    jacksonOutput.put(key.toString(), val);
                } else {
                    jacksonOutput.put('_' + key.toString(), val);
        }
    }

    public String toString() {
        addEventData(GELF.FACILITY, this.getLoggerName(), true);
        addEventData(ROOT.LEVEL, this.getLevelInt(), true);
        addEventData(GELF.HOST, this.getHostname(), true);
        addEventData(ROOT.USER, this.getUsername());
        addEventData(ROOT.NDC, this.getNdc());
        addEventData(ROOT.TAGS, this.getTags());
        addEventData(ROOT.THREAD, this.getThreadName());
        final Map<IFieldName, Object> fields = this.getFields();
        if (null != fields) {
            for (final Map.Entry<IFieldName, Object> entry : fields.entrySet()) {
                addEventData(entry.getKey(), entry.getValue());
            }
        }
        addEventData(ROOT.MDC, this.getMdc());
        addEventData(GELF.VERSION, this.getVersion(), true);
        addEventData(GELF.TIMESTAMP, String.valueOf(this.getTimestamp()), true);
        addEventData(GELF.SHORTMESSAGE, this.getMessage(), true);
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
