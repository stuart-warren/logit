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
public final class LocationField extends Field {
    
private static final LocationField field = new LocationField();
    
    public LocationField() {
        this.section = ROOT.LOCATION;
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
        strBuf.append(get(LF.CLASS));
        strBuf.append(".");
        strBuf.append(get(LF.METHOD));
        strBuf.append("(");
        strBuf.append(get(LF.FILE));
        strBuf.append(":");
        strBuf.append(get(LF.LINE));
        strBuf.append(")");
        return strBuf.toString();
    }
    
    public static enum LF implements IFieldName {
        /**
         * FILE - file_name
         * File name log occured in.
         */
        FILE("file_name"),
        /**
         * CLASS - class_name
         * Class name log occured in.
         */
        CLASS("class_name"),
        /**
         * METHOD - method_name
         * Method name log occured in.
         */
        METHOD("method_name"),
        /**
         * LINE - line_number
         * Line number log occured on.
         */
        LINE("line_number");
        
        private String text;
        
        LF(String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
