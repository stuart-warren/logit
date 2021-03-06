/**
 * 
 */
package com.stuartwarren.logit.fields;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public final class LocationField extends Field {
    
    private static final LocationField FIELD = new LocationField();
    
    private LocationField() {
        this.setSection(ROOT.LOCATION);
        Field.register(this);
    }
    
    public final static void put(final IFieldName key, final String s) {
        FIELD.put0(key, s);
    }
    
    public static Object get(final IFieldName key) {
        return FIELD.get0(key);
    }
    
    public static void clear() {
        FIELD.clear0();
    }
    
    public String toString() {
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(get(LOCATION.CLASS));
        strBuf.append('.');
        strBuf.append(get(LOCATION.METHOD));
        strBuf.append('(');
        strBuf.append(get(LOCATION.FILE));
        strBuf.append(':');
        strBuf.append(get(LOCATION.LINE));
        strBuf.append(')');
        return strBuf.toString();
    }
    
    public static enum LOCATION implements IFieldName {
        /**
         * FILE - file_name<br/>
         * File name log occured in.
         */
        FILE("file_name"),
        /**
         * CLASS - class_name<br/>
         * Class name log occured in.
         */
        CLASS("class_name"),
        /**
         * METHOD - method_name<br/>
         * Method name log occured in.
         */
        METHOD("method_name"),
        /**
         * LINE - line_number<br/>
         * Line number log occured on.
         */
        LINE("line_number");
        
        private String text;
        
        LOCATION(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
