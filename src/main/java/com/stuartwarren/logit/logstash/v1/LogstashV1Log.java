/**
 * 
 */
package com.stuartwarren.logit.logstash.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.fields.IFieldName;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.layout.MicroTimestamp;
import com.stuartwarren.logit.logstash.LogstashField.LOGSTASH;
import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public final class LogstashV1Log extends Log {


    private String                  version;

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
    
    public ByteArrayOutputStream toJson() throws IOException {
        JsonFactory jsonF = new JsonFactory();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JsonGenerator jg;
        jg = jsonF.createGenerator(os, JsonEncoding.UTF8);
        // Should really just extend ObjectCodec rather than use ObjectMapper here...
        jg.setCodec(new ObjectMapper());
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
        if (null != this.getMdc())
            jg.writeObjectField(ROOT.MDC.toString(), this.getMdc());
        for(Entry<IFieldName, Object> field: this.getFields().entrySet()) {
            IFieldName key = field.getKey();
            Object value = field.getValue();
            jg.writeFieldName(key.toString());
            jg.writeObject(value);
        }
        jg.writeEndObject();
        jg.close();
        return os;
    }
    
    public String toString() {
        try {
            return toJson().toString("UTF-8");
        } catch (IOException e) {
            LogitLog.error("Failed to create JSON", e);
            return "{\"@version\": \"1\", \"@timestamp\": \"" + MicroTimestamp.INSTANCE.get(this.getTimestamp()) + "\", \"message\": \"Error creating JSON\"}";
        }
    }
}
