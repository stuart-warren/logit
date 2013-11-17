/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.HashMap;
import java.util.Map;

import com.stuartwarren.logit.utils.ThreadLocalMap;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public class Field {
    

    private static final Field FIELD = new Field();
    private transient Object tlm;
    private IFieldName section;
    
    public Field() {
        tlm = new ThreadLocalMap();
    }
    
    public static void put(final String key, final Object o) {
        if (FIELD != null) {
            FIELD.put0(key, o);
        }
    }
    
    public static Object get(final String key) {
        if (FIELD != null) {
            return FIELD.get0(key);
        }
        return null;
    }
    
    public static void remove(final String key) {
        if (FIELD != null) {
            FIELD.remove0(key);
        }
    }
    
    public static Map<String, Object> getContext() {
        if (FIELD == null) {
            return null;
        } else {
            return FIELD.getContext0();
        }
    }
    
    public static void clear() {
        if (FIELD != null) {
            FIELD.clear0();
        }
    }
    
    public String getSection() {
        return this.section.toString();
    }
    
    /**
     * @param section the section to set
     */
    public void setSection(final IFieldName section) {
        this.section = section;
    }
    
    @SuppressWarnings("unchecked")
    protected void put0(final String key, final Object o) {
        if (tlm == null) {
            return;
        } else {
            Map<String, Object> ht = (HashMap<String, Object>) ((ThreadLocalMap) tlm).get();
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

    protected void remove0(final String key) {
        if (tlm != null) {
            final HashMap<?, ?> ht = (HashMap<?, ?>) ((ThreadLocalMap) tlm).get();
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
    protected Map<String, Object> getContext0() {
        if (tlm == null) {
            return null;
        } else {
            return (HashMap<String, Object>) ((ThreadLocalMap) tlm).get();
        }
    }

    protected void clear0() {
        if (tlm != null) {
            final Map<?, ?> ht = (HashMap<?, ?>) ((ThreadLocalMap) tlm).get();
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
         * HOSTNAME - hostname
         * Host the process is running on.
         */
        HOSTNAME("hostname"),
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
        NDC("ndc"),
        /**
         * CONFIG - config
         * Fields added in configuration file
         */
        CONFIG("config"),
        /**
         * LOGIT - logit
         * Version of Logit sending this log
         */
        LOGIT("logit")
        ;
        
        private String text;
        
        ROOT(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
