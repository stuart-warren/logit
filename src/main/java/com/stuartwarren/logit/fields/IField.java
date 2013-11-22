/**
 * 
 */
package com.stuartwarren.logit.fields;

import java.util.Map;

/**
 * @author Stuart Warren 
 * @date 21 Nov 2013
 *
 */
public interface IField {
    
    public Object get0(IFieldName key);
    
    public IFieldName getSection();
    
    public String toString();
    
    public Map<IFieldName, Object> getContext0();
    
    public void clear0();

}
