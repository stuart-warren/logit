/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stuartwarren.logit.utils.LogitLog;
import com.stuartwarren.logit.utils.ThreadLocalMap;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public class Field implements IField {
    

    private static final Field FIELD = new Field();
    private static List<IField> fieldList = new ArrayList<IField>();
    private transient Object tlm;
    private IFieldName section;
    
    
    public Field() {
        tlm = new ThreadLocalMap();
    }
    
    public static void put(final IFieldName key, final Object o) {
        if (FIELD != null) {
            FIELD.put0(key, o);
        }
    }
    
    public static Object get(final IFieldName key) {
        if (FIELD != null) {
            return FIELD.get0(key);
        }
        return null;
    }
    
    public static void remove(final IFieldName key) {
        if (FIELD != null) {
            FIELD.remove0(key);
        }
    }
    
    public static Map<IFieldName, Object> getContext() {
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
    
    public IFieldName getSection() {
        return this.section;
    }
    
    /**
     * @param section the section to set
     */
    public void setSection(final IFieldName section) {
        this.section = section;
    }
    
    public static void register(IField field) {
        int i = fieldList.indexOf(field);
        if (i >= 0) {
            LogitLog.trace("Already Registered: " + field.getClass().getCanonicalName());
        } else {
            fieldList.add(field);
            LogitLog.debug("Registered: " + field.getClass().getCanonicalName());
        }
    }
    
    public static void unRegister(IField field) {
        int i = fieldList.indexOf(field);
        if (i >= 0) {
            fieldList.remove(i);
        }
    }
  
    public static Map<IFieldName,Object> list() {
        Map<IFieldName,Object> result = new HashMap<IFieldName,Object>();
        for (int i = 0; i < fieldList.size(); i++) {
            IFieldName section = fieldList.get(i).getSection();
            result.put(section, fieldList.get(i).get0(section));
        }
        return result;
    }
    
    public static void cleanUp() {
        for (int i = 0; i < fieldList.size(); i++) {
            fieldList.get(i).clear0();
        }
    }
    
    @SuppressWarnings("unchecked")
    protected void put0(final IFieldName key, final Object o) {
        if (tlm == null) {
            return;
        } else {
            Map<IFieldName, Object> ht = (HashMap<IFieldName, Object>) ((ThreadLocalMap) tlm).get();
            if (ht == null) {
                ht = new HashMap<IFieldName, Object>();
                ((ThreadLocalMap) tlm).set(ht);
            }
            HashMap<IFieldName, Object> h = (HashMap<IFieldName, Object>) get0(getSection());
            if (h == null) {
                h = new HashMap<IFieldName, Object>();
            }
            h.put(key, o);
            ht.put(getSection(), h);
        }
    }

    public Object get0(IFieldName key) {
        if (tlm == null) {
            return null;
        } else {
            @SuppressWarnings("unchecked")
            HashMap<IFieldName, Object> ht = (HashMap<IFieldName, Object>) ((ThreadLocalMap) tlm).get();
            if (ht != null && key != null) {
                return ht.get(key);
            } else {
                return null;
            }
        }
    }

    public void remove0(final IFieldName key) {
        if (tlm != null) {
            final HashMap<?, ?> ht = (HashMap<?, ?>) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.remove(key);
                // clean up if this was the last key
                if (ht.isEmpty()) {
                        LogitLog.trace("Last key removed, cleaning up.");
                        clear0();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Map<IFieldName, Object> getContext0() {
        if (tlm == null) {
            return null;
        } else {
            return (HashMap<IFieldName, Object>) ((ThreadLocalMap) tlm).get();
        }
    }

    public void clear0() {
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
        LOGIT("logit"),
        /**
         * HTTP - http
         * HTTP specific fields
         */
        HTTP("http")
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
