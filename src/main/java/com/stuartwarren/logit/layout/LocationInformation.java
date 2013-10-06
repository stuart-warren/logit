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
public class LocationInformation {
    
    private String fileName;
    private String className;
    private String methodName;
    private String lineNumber;
    
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(getClassName());
        strBuf.append(".");
        strBuf.append(getMethodName());
        strBuf.append("(");
        strBuf.append(getFileName());
        strBuf.append(":");
        strBuf.append(getLineNumber());
        strBuf.append(")");
        return strBuf.toString();
    }
    
    public Map<String,String> toHashMap() {
        Map<String,String> map = new HashMap<String, String>();
        map.put("class_name", getClassName());
        map.put("method_name", getMethodName());
        map.put("file_name", getFileName());
        map.put("line_number", getLineNumber());
        return map;
    }
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }
    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }
    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }
    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    /**
     * @return the lineNumber
     */
    public String getLineNumber() {
        return lineNumber;
    }
    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

}
