/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.HashMap;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public final class ExceptionField extends Field {
    
    private static final ExceptionField field = new ExceptionField();
    
    public ExceptionField() {
        this.section = RF.EXCEPTION;
    }
    
    public final static void put(IFieldName key, String s) {
        if (field != null) {
            field.put0(key.toString(), s);
        }
    }
    
    public static Object get(IFieldName key) {
        if (field != null) {
            return field.get0(key.toString());
        }
        return null;
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
    
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
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
        
        EF(String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
