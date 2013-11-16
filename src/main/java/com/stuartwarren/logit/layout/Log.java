/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.stuartwarren.logit.fields.Field.ROOT;
import com.stuartwarren.logit.fields.IFieldName;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Log {
    
    private static CachedDetails    details = CachedDetails.getInstance();
    private long                    timestamp;
    private String                  ndc;
    private Map<String, Object>     mdc;
    private String                  level;
    private int                     levelInt;
    private String                  loggerName;
    private String                  threadName;
    private String                  message;
    private List<String>            tags;
    private Map<IFieldName,Object>      fields;
    private transient final String  user = details.getUsername();
    private transient final String  hostname = details.getHostname();
    private transient final String  logitVersion = details.getVersion();

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
     * @return the ndc
     */
    public String getNdc() {
        return ndc;
    }

    /**
     * @param ndc
     *            the ndc to set
     */
    public void setNdc(final String ndc) {
        if (null != ndc) {
            this.ndc = ndc;
        }
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
    public void setMdc(final Map<String, Object> properties) {
        this.mdc = properties;
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
    public void setMessage(final String message) {
        if (null != message) {
            this.message = message;
        }
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
    public void setLevel(final String level) {
        if (null != level) {
            this.level = level;
        }
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
    public void setLevelInt(final int levelInt) {
        this.levelInt = levelInt;
    }

    /**
     * @return the loggerNameString
     */
    public String getLoggerName() {
        return loggerName;
    }

    /**
     * @param loggerNameString
     *            the loggerNameString to set
     */
    public void setLoggerName(final String loggerName) {
        if (null != loggerName) {
            this.loggerName = loggerName;
        }
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
    public void setThreadName(final String threadName) {
        if (null != threadName) {
            this.threadName = threadName;
        }
    }
    
    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags 
     *      the tags to set
     */
    public void setTags(final String tags) {
        // Split string on commas. Ignore whitespace.
        if (null != tags) {
            this.tags = new ArrayList<String>(Arrays.asList(tags.split("\\s*,\\s*")));
        }
    }
    
    /**
     * @param tag 
     *      the tag to add to the list
     */
    public void appendTag(final String tag) {
        if (null != tag) {
            if (null == this.tags) {
                this.tags = new ArrayList<String>();
            }
            this.tags.add(tag);
        }
    }

    /**
     * @return the fields
     */
    public Map<IFieldName,Object> getFields() {
        return fields;
    }
    
    public String getUsername() {
        return user;
    }
    
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
     * @param fields the fields to set
     */
    public void setFields(final String fields) {
        // Split string on : and ,
        //eg field1:value,field2:value
        if (null == this.fields) {
            this.fields = new LinkedHashMap<IFieldName, Object>();
        }
        final Map<String,String> configFields = new HashMap<String,String>();
        if (null != fields) {
            for(final String keyValue : fields.split("\\s*,\\s*")) {
                final String[] pairs = keyValue.split("\\s*:\\s*", 2);
                configFields.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
            }
            addField(ROOT.CONFIG, configFields);
        }

    }
    
    @SuppressWarnings("unchecked")
    public void addField(final IFieldName key, final Object val) {
    	if (null == fields) {
            this.fields = new LinkedHashMap<IFieldName, Object>();
        }
        if (val instanceof HashMap) {
            if (!((HashMap<String, Object>) val).isEmpty()) {
                if (null != val) {
                    fields.put(key,(HashMap<String, Object>) val);
                }
            }
        } else if (null != val) {
            fields.put(key, val);
        }
    }

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

}
