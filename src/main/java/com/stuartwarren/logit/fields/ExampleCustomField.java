/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 * <br/>
 * An Example custom field to use as a template.<br>
 * Basic usage would be similar to:<br/>
 * <pre>
 * ExampleCustomField.put(EXAMPLE.MYFIELD, "Value of field");
 * logger.info("Thing to log");
 * ExampleCustomField.clear();
 * </pre>
 *
 */
public final class ExampleCustomField extends Field {
    
    // Create a new static instance of your class to use later
    private static final ExampleCustomField FIELD = new ExampleCustomField();
    
    /**
     * Private constructor
     */
    private ExampleCustomField() {
        // Choose and set a section name for your base object (IFieldName)
        this.setSection(EXAMPLE.DEMO);
        // Register this custom field object so it can be added to the log during 
        // serialisation.
        Field.register(this);
    }
    
    /**
     * Create a new field within a root object. Fields are NOT automatically cleared
     * after use. See clear() method.
     * @param key
     *      FieldName to create, must be a documented enum implimenting IFieldName
     *      to maximise ease of reuse.
     * @param s
     *      Value of field, should be any type easily serialisable by Jackson:<br/>
     *          {@code LinkedHashMap<String,Object>}, {@code ArrayList<String>}, 
     *          {@code String}, {@code Integer, Long, BigInteger},  
     *          {@code Double} or {@code Boolean}.
     */
    
    public static <K, V> void put(final IFieldName key, final LinkedHashMap<K,V> o) {
      FIELD.put0(key, o);
    }
    
    public static void put(final IFieldName key, final ArrayList<String> o) {
      FIELD.put0(key, o);
    }
    
    public static void put(final IFieldName key, final String o) {
      FIELD.put0(key, o);
    }
    
    public static void put(final IFieldName key, final int o) {
      FIELD.put0(key, o);
    }
    
    public static void put(final IFieldName key, final long o) {
      FIELD.put0(key, o);
    }
    
    public static void put(final IFieldName key, final double o) {
      FIELD.put0(key, o);
    }
    
    public static void put(final IFieldName key, final boolean o) {
      FIELD.put0(key, o);
    }
    
    /**
     * 
     * @param key
     *      FieldName to fetch the value of.
     * @return
     *      Value of requested field.
     */
    public static Object get(final IFieldName key) {
        return FIELD.get0(key);
    }
    
    /**
     * Clear all stored fields to stop values being inserted into later logs.
     */
    public static void clear() {
        FIELD.clear0();
    }
    
    @Override
    public String toString() {
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(get(EXAMPLE.DEMO));
        return strBuf.toString();
    }
    
    /**
     * 
     * @author Stuart Warren 
     * @date 11 Jan 2014
     * <br/>
     * Documented enum used for fieldnames.<br/>
     * Create your own.
     */
    public static enum EXAMPLE implements IFieldName {
        /**
         * DEMO - demo_root_field<br/>
         * Purely an example. Don't use this.
         */
        DEMO("demo_field"),
        /**
         * MYFIELD - my_field<br/>
         * Example of a field name.
         */
        MYFIELD("my_field");
        
        private String text;
        
        EXAMPLE(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
