/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.Map;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public final class ExceptionField extends Field {
    
    private static final ExceptionField FIELD = new ExceptionField();
    
    public ExceptionField() {
        super();
        this.setSection(RF.EXCEPTION);
    }
    
    public final static void put(final IFieldName key, final String s) {
        if (FIELD != null) {
            FIELD.put0(key.toString(), s);
        }
    }
    
    public static Object get(final IFieldName key) {
        if (FIELD != null) {
            return FIELD.get0(key.toString());
        }
        return null;
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
    
    public String toString() {
        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(get(EF.CLASS));
        strBuf.append(": ");
        strBuf.append(get(EF.MESSAGE));
        strBuf.append("/n");
        strBuf.append(get(EF.STACKTRACE));
        strBuf.append("/n");
        return strBuf.toString();
    }
    
    public static enum EF implements IFieldName {
        /**
         * CLASS - exeption_class
         * Class in which exception occured.
         */
        CLASS("exception_class"),
        /**
         * MESSAGE - exception_message
         * Message passed as part of exception.
         */
        MESSAGE("exception_message"),
        /**
         * STACKTRACE - stacktrace
         * Full thrown stacktrace
         */
        STACKTRACE("stacktrace");
        
        private String text;
        
        EF(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
