/**
 * 
 */
package com.stuartwarren.logit.fields;

/**
 * @author Stuart Warren 
 * @date 20 Oct 2013
 *
 */
public final class ExceptionField extends Field {
    
    private static final ExceptionField FIELD = new ExceptionField();
    
    public ExceptionField() {
        super();
        this.setSection(ROOT.EXCEPTION);
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
    
    public static void clear() {
        if (FIELD != null) {
            FIELD.clear0();
        }
    }
    
    public String toString() {
        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(get(EXCEPTION.CLASS));
        strBuf.append(": ");
        strBuf.append(get(EXCEPTION.MESSAGE));
        strBuf.append("/n");
        strBuf.append(get(EXCEPTION.STACKTRACE));
        strBuf.append("/n");
        return strBuf.toString();
    }
    
    public static enum EXCEPTION implements IFieldName {
        /**
         * CLASS - exeption_class<br/>
         * Class in which exception occured.
         */
        CLASS("exception_class"),
        /**
         * MESSAGE - exception_message<br/>
         * Message passed as part of exception.
         */
        MESSAGE("exception_message"),
        /**
         * STACKTRACE - stacktrace<br/>
         * Full thrown stacktrace
         */
        STACKTRACE("stacktrace");
        
        private String text;
        
        EXCEPTION(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
