/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.stuartwarren.logit.fields.IFieldName;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Log {
    
    private CachedDetails           details = CachedDetails.getInstance();
    private long                    timestamp;
    private String                  ndc;
    private Map<String, Object>     mdc;
    private String                  level;
    private int                     level_int;
    private String                  loggerName;
    private String                  threadName;
    private String                  message;
    private ArrayList<String>       tags = null;
    private Map<String,Object>      fields = new HashMap<String,Object>();
    private String                  user = details.getUsername();
    private String                  hostname = details.getHostname();

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
     * @return the ndc
     */
    public String getNdc() {
        return ndc;
    }

    /**
     * @param ndc
     *            the ndc to set
     */
    public void setNdc(String ndc) {
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
    public void setMdc(Map<String, Object> properties) {
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
    public void setMessage(String message) {
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
    public void setLevel(String level) {
        if (null != level) {
            this.level = level;
        }
    }

    /**
     * @return the level_int
     */
    public int getLevel_int() {
        return level_int;
    }

    /**
     * @param level_int the level_int to set
     */
    public void setLevel_int(int level_int) {
        this.level_int = level_int;
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
    public void setLoggerName(String loggerName) {
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
    public void setThreadName(String threadName) {
        if (null != threadName) {
            this.threadName = threadName;
        }
    }
    
    /**
     * @return the tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * @param tags 
     *      the tags to set
     */
    public void setTags(String tags) {
        // Split string on commas. Ignore whitespace.
        if (null != tags) {
            this.tags = new ArrayList<String>(Arrays.asList(tags.split("\\s*,\\s*")));
        }
    }
    
    /**
     * @param tag 
     *      the tag to add to the list
     */
    public void appendTag(String tag) {
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
    public Map<String,Object> getFields() {
        return fields;
    }
    
    public String getUsername() {
        return user;
    }
    
    public String getHostname() {
        return hostname;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(String fields) {
        // Split string on : and ,
        //eg field1:value,field2:value
        if (null == fields) {
            this.fields = new LinkedHashMap<String, Object>();
        }
        for(String keyValue : fields.split("\\s*,\\s*")) {
            String[] pairs = keyValue.split("\\s*:\\s*", 2);
            this.fields.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
        }

    }
    
    @SuppressWarnings("unchecked")
    public void addField(IFieldName key, Object val) {
        if (val instanceof HashMap) {
            if (!((HashMap<String, Object>) val).isEmpty()) {
                if (null != val) {
                    fields.put(key.toString(), val);
                }
            }
        } else if (null != val) {
            fields.put(key.toString(), val);
        }
    }
    
    public void addField(HashMap<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    fields.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
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
