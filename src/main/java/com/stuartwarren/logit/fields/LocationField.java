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
public final class LocationField extends Field {
    
private static final LocationField FIELD = new LocationField();
    
    public LocationField() {
        super();
        this.setSection(ROOT.LOCATION);
        Field.register(this);
    }
    
    public final static void put(final IFieldName key, final String s) {
        if (FIELD != null) {
            FIELD.put0(key, s);
        }
    }
    
    public static Object get(final IFieldName key) {
        if (FIELD != null) {
            return FIELD.get0(key);
        }
        return null;
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
    
    public String toString() {
        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(get(LF.CLASS));
        strBuf.append('.');
        strBuf.append(get(LF.METHOD));
        strBuf.append('(');
        strBuf.append(get(LF.FILE));
        strBuf.append(':');
        strBuf.append(get(LF.LINE));
        strBuf.append(')');
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
        
        LF(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
