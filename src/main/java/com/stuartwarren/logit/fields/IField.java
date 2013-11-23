/**
 * 
 */
package com.stuartwarren.logit.fields;

/**
 * @author Stuart Warren 
 * @date 21 Nov 2013
 * <br/>
 * A custom field to add to a log.<br/>
 * Users can create their own custom fields classes
 * and register them so they are added to the log 
 * automatically.<br/>
 * <br/>
 * To do so, you need to extend the Field class and 
 * add a few static properties and methods, eg:<br/>
 * <pre>
 *   private static final ExceptionField FIELD = new ExceptionField();
 *   
 *   public ExceptionField() {
 *       super();
 *       this.setSection(ROOT.EXCEPTION);
 *       Field.register(this);
 *   }
 *   
 *   public final static void put(final IFieldName key, final String s) {
 *       if (FIELD != null) {
 *           FIELD.put0(key, s);
 *       }
 *   }
 *   
 *   public static Object get(final IFieldName key) {
 *       if (FIELD != null) {
 *           return FIELD.get0(key);
 *       }
 *       return null;
 *   }
 *
 *   public static void clear() {
 *       if (FIELD != null) {
 *           FIELD.clear0();
 *       }
 *   }
 * </pre>  
 * You can also add a static inner IFieldName enum to your field. eg:
 * <pre>
 *     public static enum EXCEPTION implements IFieldName {
 *       // Don't forget to javadoc each entry
 *       CLASS("exception_class"),
 *       // Don't forget to javadoc each entry
 *       MESSAGE("exception_message"),
 *       // Don't forget to javadoc each entry
 *       STACKTRACE("stacktrace");
 *       
 *       private String text;
 *       
 *       EXCEPTION(final String text) {
 *           this.text = text;
 *       }
 *       
 *       public String toString() {
 *           return this.text;
 *       }
 *       
 *   }
 * </pre>
 */
public interface IField {
    
    public IFieldName getSection();
    
    public String toString();
    
    public Object get0(IFieldName key);
    
    public void clear0();

}
