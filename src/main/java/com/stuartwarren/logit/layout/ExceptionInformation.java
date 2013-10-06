/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class ExceptionInformation {
    
    private String exceptionClass;
    private String exceptionMessage;
    private String stackTrace;
    
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(getExceptionClass());
        strBuf.append(": ");
        strBuf.append(getExceptionMessage());
        strBuf.append("/n");
        strBuf.append(getStackTrace());
        strBuf.append("/n");
        return strBuf.toString();
    }
    
    public Map<String,String> toHashMap() {
        Map<String,String> map = new HashMap<String, String>();
        map.put("exception_class", getExceptionClass());
        map.put("exception_message", getExceptionMessage());
        map.put("stack_trace", getStackTrace());
        return map;
    }
    
    /**
     * @return the exceptionClass
     */
    public String getExceptionClass() {
        return exceptionClass;
    }
    /**
     * @param exceptionClass the exceptionClass to set
     */
    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }
    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }
    /**
     * @param exceptionMessage the exceptionMessage to set
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
    /**
     * @return the stackTrace
     */
    public String getStackTrace() {
        return stackTrace;
    }
    /**
     * @param stackTrace the stackTrace to set
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
    
    

}
