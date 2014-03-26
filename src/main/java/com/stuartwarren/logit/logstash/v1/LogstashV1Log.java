/**
 * 
 */
package com.stuartwarren.logit.logstash.v1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.logstash.LogstashField.LOGSTASH;
import com.stuartwarren.logit.utils.LogitLog;

import org.apache.commons.lang3.text.StrBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public final class LogstashV1Log extends Log {

    private ObjectMapper objectMapper;

    private String                  version;

    public LogstashV1Log(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
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
    public Log setVersion(final String version) {
        this.version = version;
        return this;
    }
    
    public String toJson() throws IOException {
        StrBuilder sb = new StrBuilder();
        JsonFactory jsonF = new JsonFactory();
        JsonGenerator jg = jsonF.createGenerator(sb.asWriter());
        jg.setCodec(objectMapper);
        jg.writeStartObject();
        jg.writeStringField(LOGSTASH.VERSION.toString(), this.getVersion());
        jg.writeStringField(LOGSTASH.TIMESTAMP.toString(), this.getStrTimestamp());
        jg.writeStringField(ROOT.MESSAGE.toString(), this.getMessage());
        jg.writeStringField(ROOT.LEVEL.toString(), this.getLevel());
        jg.writeStringField(ROOT.USER.toString(), this.getUsername());
        jg.writeStringField(ROOT.HOSTNAME.toString(), this.getHostname());
        jg.writeStringField(ROOT.LOGGER.toString(), this.getLoggerName());
        jg.writeStringField(ROOT.LOGIT.toString(), this.getLogitVersion());
        jg.writeStringField(ROOT.THREAD.toString(), this.getThreadName());
        jg.writeArrayFieldStart(ROOT.TAGS.toString());
            for(String tag: this.getTags())
                jg.writeString(tag);
        jg.writeEndArray();
        if (null != this.getNdc())
            jg.writeStringField(ROOT.NDC.toString(), this.getNdc());
        jg.writeObjectFieldStart(ROOT.MDC.toString());
        writeFieldMap(jg, this.getMdc());
        jg.writeEndObject();
        writeFieldMap(jg, this.getFields());
        jg.writeEndObject();
        jg.close();
        return sb.toString();
    }
    
    /**
     * @param jg
     * @param value
     */
    private <E> void writeFieldList(JsonGenerator jg, List<E> list) throws IOException {
        jg.writeStartArray();
        for(E tag: list)
            jg.writeString((String) tag);
        jg.writeEndArray();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <K, V> void writeFieldMap(JsonGenerator jg, Map<K, V> map) throws IOException {
        if (null != map) {
            for (Map.Entry<K, V> field : map.entrySet()) {
                K key = field.getKey();
                V value = field.getValue();
                jg.writeFieldName(key.toString());
                if (value instanceof String) {
                    jg.writeString((String) value);
                } else if (value instanceof Map) {
                    jg.writeStartObject();
                    writeFieldMap(jg, (Map) value);
                    jg.writeEndObject();
                } else if (value instanceof List) {
                    writeFieldList(jg, (List) value);
                } else {
                    jg.writeObject(value);
                }
            }
        }
    }

    public String toString() {
        try {
            return toJson();
        } catch (IOException e) {
            LogitLog.error("Failed to create JSON", e);
            return "{\"@version\": \"1\", \"@timestamp\": \"" + this.getStrTimestamp() + "\", \"message\": \"Error creating JSON\"}";
        }
    }
}
