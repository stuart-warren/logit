/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.HashMap;

import com.stuartwarren.logit.utils.LogitLog;
import com.stuartwarren.logit.utils.ThreadLocalMap;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public class Field {
    
    private static final Field field = new Field();
    private Object tlm;
    public IFieldName section;
    
    public Field() {
        try {
            this.section = null;
            tlm = new ThreadLocalMap();
        } catch (Exception e) {
            LogitLog.error("Error thrown initialising ExceptionField", e);
        }
    }
    
    public static void put(String key, Object o) {
        if (field != null) {
            field.put0(key, o);
        }
    }
    
    public static Object get(String key) {
        if (field != null) {
            return field.get0(key);
        }
        return null;
    }
    
    public static void remove(String key) {
        if (field != null) {
            field.remove0(key);
        }
    }
    
    public static HashMap<String, Object> getContext() {
        if (field != null) {
            return field.getContext0();
        } else {
            return null;
        }
    }
    
    public static void clear() {
        if (field != null) {
            field.clear0();
        }
    }
    
    public String getSection() {
        return this.section.toString();
    }
    
    @SuppressWarnings("unchecked")
    protected void put0(String key, Object o) {
        if (tlm == null) {
            return;
        } else {
            HashMap<String, Object> ht = (HashMap<String, Object>) ((ThreadLocalMap) tlm).get();
            if (ht == null) {
                ht = new HashMap<String, Object>();
                ((ThreadLocalMap) tlm).set(ht);
            }
            HashMap<String, Object> h = (HashMap<String, Object>) get0(getSection());
            if (h == null) {
                h = new HashMap<String, Object>();
            }
            h.put(key, o);
            ht.put(getSection(), h);
        }
    }

    protected Object get0(String key) {
        if (tlm == null) {
            return null;
        } else {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> ht = (HashMap<String, Object>) ((ThreadLocalMap) tlm).get();
            if (ht != null && key != null) {
                return ht.get(key);
            } else {
                return null;
            }
        }
    }

    protected void remove0(String key) {
        if (tlm != null) {
            HashMap<?, ?> ht = (HashMap<?, ?>) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.remove(key);
                // clean up if this was the last key
                if (ht.isEmpty()) {
                        clear0();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected HashMap<String, Object> getContext0() {
        if (tlm == null) {
            return null;
        } else {
            return (HashMap<String, Object>) ((ThreadLocalMap) tlm).get();
        }
    }

    protected void clear0() {
        if (tlm != null) {
            HashMap<?, ?> ht = (HashMap<?, ?>) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.clear();
            }
        }
    }
    
    public static enum ROOT implements IFieldName {
        /**
         * MESSAGE - message
         * Main log message.
         */
        MESSAGE("message"),
        /**
         * TAGS - tags
         * List of tags to assist filtering.
         */
        TAGS("tags"),
        /**
         * USER - user
         * Name of user running the process.
         */
        USER("user"),
        /**
         * HOSTNAME - host_name
         * Host the process is running on.
         */
        HOSTNAME("host_name"),
        /**
         * LOCATION - location
         * Location information of where in the code this log is.
         */
        LOCATION("location"),
        /**
         * EXCEPTION - exception
         * Exception details including full stacktrace.
         */
        EXCEPTION("exception"),
        /**
         * THREAD - thread
         * Details of current thread
         */
        THREAD("thread"),
        /**
         * LOGGER - logger
         * Details of current logger.
         */
        LOGGER("logger"),
        /**
         * LEVEL - level
         * Used to denote how severe the event is.
         */
        LEVEL("level"),
        /**
         * MDC - mdc
         * Mapped Diagnostic Context
         */
        MDC("mdc"),
        /**
         * NDC - ndc
         * Nested Diagnostic Context
         */
        NDC("ndc")
        ;
        
        private String text;
        
        ROOT(String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
