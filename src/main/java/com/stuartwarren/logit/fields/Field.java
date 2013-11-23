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
    
    /**
     * Add a key and value to this field<br/>
     * @param key must be an IFieldName
     * @param o can be any object accepted by the Jackson JSON library
     */
    public static void put(final IFieldName key, final Object o) {
        if (FIELD != null) {
            FIELD.put0(key, o);
        }
    }
    
    /**
     * 
     * @param key must be an IFieldName
     * @return the value of the key specified
     */
    public static Object get(final IFieldName key) {
        if (FIELD != null) {
            return FIELD.get0(key);
        }
        return null;
    }
    
    /**
     * Remove this entry from the field
     * @param key must be an IFieldName
     */
    public static void remove(final IFieldName key) {
        if (FIELD != null) {
            FIELD.remove0(key);
        }
    }
    
    /**
     * 
     * @return the entire Map of keys and values for this field
     */
    public static Map<IFieldName, Object> getContext() {
        if (FIELD == null) {
            return null;
        } else {
            return FIELD.getContext0();
        }
    }
    
    /**
     * Clear all the keys from this field<br/>
     * If not cleared they will be present in later log messages
     */
    public static void clear() {
        if (FIELD != null) {
            FIELD.clear0();
        }
    }
    
    /**
     * @return the section name of this field
     */
    public IFieldName getSection() {
        return this.section;
    }
    
    /**
     * The section is the visible name of the field
     * @param section the section to set
     */
    public void setSection(final IFieldName section) {
        this.section = section;
    }
    
    /**
     * Registering an IField object allows it to be added automatically to logs<br/>
     * @see Log.addRegisteredFields()
     * @param field Usually just pass in 'this'
     */
    public static void register(IField field) {
        int i = fieldList.indexOf(field);
        if (i >= 0) {
            LogitLog.trace("Already Registered: " + field.getClass().getCanonicalName());
        } else {
            fieldList.add(field);
            LogitLog.debug("Registered: " + field.getClass().getCanonicalName());
        }
    }
    
    /**
     * Unregister a registered IField from being auto added to a log.
     * @param field
     */
    public static void unRegister(IField field) {
        int i = fieldList.indexOf(field);
        if (i >= 0) {
            fieldList.remove(i);
        }
    }
  
    /**
     * 
     * @return a Map of all the registered IField objects and their sections
     */
    public static Map<IFieldName,Object> list() {
        Map<IFieldName,Object> result = new HashMap<IFieldName,Object>();
        for (int i = 0; i < fieldList.size(); i++) {
            IFieldName section = fieldList.get(i).getSection();
            result.put(section, fieldList.get(i).get0(section));
        }
        return result;
    }
    
    /**
     * Run Field.clear() on all registered IField objects
     */
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
    protected Map<IFieldName, Object> getContext0() {
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

    /**
     * 
     * @author Stuart Warren 
     * @date 22 Nov 2013
     * An enum of fieldnames to be used when building a log<br/>
     * Enums are used in order to have a documented list of field names<br/>
     * rather than random strings scattered through code with mispellings.<br/>
     * This assists users to use existing fieldnames rather than creating their own.
     */
    public static enum ROOT implements IFieldName {
        /**
         * MESSAGE - message<br/>
         * Main log message.
         */
        MESSAGE("message"),
        /**
         * TAGS - tags<br/>
         * List of tags to assist filtering.
         */
        TAGS("tags"),
        /**
         * USER - user<br/>
         * Name of user running the process.
         */
        USER("user"),
        /**
         * HOSTNAME - hostname<br/>
         * Host the process is running on.
         */
        HOSTNAME("hostname"),
        /**
         * LOCATION - location<br/>
         * Location information of where in the code this log is.
         */
        LOCATION("location"),
        /**
         * EXCEPTION - exception<br/>
         * Exception details including full stacktrace.
         */
        EXCEPTION("exception"),
        /**
         * THREAD - thread<br/>
         * Details of current thread
         */
        THREAD("thread"),
        /**
         * LOGGER - logger<br/>
         * Details of current logger.
         */
        LOGGER("logger"),
        /**
         * LEVEL - level<br/>
         * Used to denote how severe the event is.
         */
        LEVEL("level"),
        /**
         * MDC - mdc<br/>
         * Mapped Diagnostic Context
         */
        MDC("mdc"),
        /**
         * NDC - ndc<br/>
         * Nested Diagnostic Context
         */
        NDC("ndc"),
        /**
         * CONFIG - config<br/>
         * Fields added in configuration file
         */
        CONFIG("config"),
        /**
         * LOGIT - logit<br/>
         * Version of Logit sending this log
         */
        LOGIT("logit"),
        /**
         * HTTP - http<br/>
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
