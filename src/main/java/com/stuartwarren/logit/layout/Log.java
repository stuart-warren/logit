/**
 * 
 */
package com.stuartwarren.logit.layout;

import com.stuartwarren.logit.fields.Field;
import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.fields.IFieldName;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 * <br/>
 * Base Log object.<br/>
 * Can be extended/specialized to handle extra functionality.<br/>
 * Each ILayout implementing class specifies which Log class they use.<br/>
 * Users specify the layout in config and the LayoutFactory creates the right log type.
 */
public class Log {
    
    private long                    timestamp;
    private String                  strTimestamp;
    private String                  ndc;
    private Map<String, Object>     mdc;
    private String                  level;
    private int                     levelInt;
    private String                  loggerName;
    private String                  threadName;
    private String                  message;
    private List<String>            tags;
    private Map<IFieldName,Object>  fields;
    private transient final String  user = CachedDetails.INSTANCE.getUsername();
    private transient final String  hostname = CachedDetails.INSTANCE.getHostname();
    private transient final String  logitVersion = CachedDetails.INSTANCE.getVersion();

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
    public Log setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        setStrTimestamp(timestamp);
        return this;
    }
    
    /**
     * @return the strTimestamp
     */
    public String getStrTimestamp() {
        return strTimestamp;
    }

    /**
     * Set to ISO8601 format timestamp.
     * Generating timestamp now due to use of {@link System.nanoTime} 
     * @param strTimestamp the strTimestamp to set
     */
    private Log setStrTimestamp(long timestamp) {
        Timestamp ts = new Timestamp(timestamp);
        this.strTimestamp = ts.toString();
        return this;
    }

    /**
     * @return the ndc
     */
    public String getNdc() {
        return ndc;
    }

    /**
     * @param ndc
     *            the ndc to set
     */
    public Log setNdc(final String ndc) {
        if (null != ndc) {
            this.ndc = ndc;
        }
        return this;
    }

    /**
     * @return the mdc
     */
    public Map<String, Object> getMdc() {
        return mdc;
    }

    /**
     * @param properties
     *            the mdc to set
     */
    public Log setMdc(final Map<String, Object> properties) {
        this.mdc = properties;
        return this;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public Log setMessage(final String message) {
        if (null != message) {
            this.message = message;
        }
        return this;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public Log setLevel(final String level) {
        if (null != level) {
            this.level = level;
        }
        return this;
    }

    /**
     * @return the level_int
     */
    public int getLevelInt() {
        return levelInt;
    }

    /**
     * @param levelInt the level_int to set
     */
    public Log setLevelInt(final int levelInt) {
        this.levelInt = levelInt;
        return this;
    }

    /**
     * @return the loggerName
     */
    public String getLoggerName() {
        return loggerName;
    }

    /**
     * @param loggerName
     *            the loggerName to set
     */
    public Log setLoggerName(final String loggerName) {
        if (null != loggerName) {
            this.loggerName = loggerName;
        }
        return this;
    }

    /**
     * @return the threadName
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * @param threadName
     *            the threadName to set
     */
    public Log setThreadName(final String threadName) {
        if (null != threadName) {
            this.threadName = threadName;
        }
        return this;
    }
    
    /**
     * 
     * @return the username
     */
    public String getUsername() {
        return user;
    }
    
    /**
     * 
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }
    
    /**
     * @return the logitVersion
     */
    public String getLogitVersion() {
        return logitVersion;
    }
    
    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }
    
    /**
     * @param tags 
     *      the tags to set<br/>
     * String split on ,<br/>
     * eg <pre>'tag1,tag2,tag3'</pre><br/>
     */
    public Log setTags(final String tags) {
        // Split string on commas. Ignore whitespace.
        if (null != tags) {
            String[] splitTags = StringUtils.split(tags, ",");
            for (String tag : splitTags) {
                this.appendTag(tag.trim());
            }
        }
        return this;
    }
    
    /**
     * Append a single tag to the log
     * @param tag 
     *      the tag to add to the list
     */
    public Log appendTag(final String tag) {
        if (null != tag) {
            if (null == this.tags) {
                this.tags = new ArrayList<String>();
            }
            this.tags.add(tag);
        }
        return this;
    }

    /**
     * @return the fields
     */
    public Map<IFieldName,Object> getFields() {
        return fields;
    }

    public Log setFields(Map<String, String> fields) {
        if (fields != null) {
            addField(ROOT.CONFIG, fields);
        }
        return this;
    }

    /**
     * @param fields the fields to set<br/>
     * List of key:value pairs<br/>
     * String split on : and ,<br/>
     * eg <pre>'field1:value,field2:value'</pre><br/>
     * Added into a config object.<br/>
     * eg <pre>"config":{"field1":"value1"}</pre>
     */
    public static Map<String, String> parseFields(final String fields) {
        final Map<String,String> configFields = new LinkedHashMap<String,String>();
        if (null != fields) {
            for (final String keyValue : StringUtils.split(fields, ",")) {
                final String[] pairs = StringUtils.split(keyValue.trim(), ":", 2);
                configFields.put(pairs[0].trim(), pairs.length == 1 ? "" : pairs[1].trim());
            }

        }
        return configFields;
    }
    
    /**
     * Add all registered fields and their values into the current log<br/>
     * Classes implementing IField should register themselves in their constructor
     */
    public Log addRegisteredFields() {
 
        // add all registered fields to log
        Map<IFieldName,Object> l = Field.list();
        Iterator<Entry<IFieldName, Object>> it = l.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<IFieldName,Object> pairs = (Map.Entry<IFieldName,Object>)it.next();
            IFieldName key = (IFieldName) pairs.getKey();
            @SuppressWarnings("unchecked")
            Map<IFieldName,Object> value = (Map<IFieldName,Object>) pairs.getValue();
            this.addField(key, value);
            it.remove(); // avoids a ConcurrentModificationException
        }
        return this;
    }

    /**
     * @return a string representation of the current log.
     */
    public String toString() {
        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(getTimestamp());
        strBuf.append(' ');
        strBuf.append(getNdc());
        strBuf.append(' ');
        strBuf.append(getMdc());
        strBuf.append(' ');
        strBuf.append(getMessage());
        strBuf.append(' ');
        strBuf.append(getLevel());
        strBuf.append(' ');
        strBuf.append(getLoggerName());
        strBuf.append(' ');
        strBuf.append(getThreadName());
        strBuf.append(' ');
        strBuf.append(getTags());
        strBuf.append(' ');
        strBuf.append(getFields());
        strBuf.append(' ');
        strBuf.append(getUsername());
        strBuf.append(' ');
        strBuf.append(getHostname());
        return strBuf.toString();
    }
    
    @SuppressWarnings("unchecked")
    private Log addField(final IFieldName key, final Object val) {
        if (null == fields) {
            this.fields = new LinkedHashMap<IFieldName, Object>();
        }
        if (val instanceof HashMap) {
            if (!((LinkedHashMap<String, Object>) val).isEmpty()) {
                if (null != val) {
                    fields.put(key,(LinkedHashMap<String, Object>) val);
                }
            }
        } else if (null != val) {
            fields.put(key, val);
        }
        return this;
    }

}
