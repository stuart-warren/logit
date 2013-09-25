/**
 * 
 */
package com.stuartwarren.logit;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Stuart Warren 
 * @date 22 Sep 2013
 *
 */
public class CommonLog {
	
//	private String hostname = getLocalHostname() ;
//  private String username = getUsername();
    private Timestamp timestamp;
    private String ndc;
    private Map<String, Object> mdc;
    private HashMap<String, Object> exceptionInformation;
    private HashMap<String, Object> locationInformation;
    private String level;
    private String loggerName;
    private String threadName;
    private String message;
	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = new Timestamp(timestamp);
	}
	/**
	 * @return the ndc
	 */
	public String getNdc() {
		return ndc;
	}
	/**
	 * @param ndc the ndc to set
	 */
	public void setNdc(String ndc) {
		if (null != ndc) {
			this.ndc = ndc;
		}
	}
	/**
	 * @return the mdc
	 */
	public Map<String, Object> getMdc() {
		return mdc;
	}
	/**
	 * @param properties the mdc to set
	 */
	public void setMdc(Map<String, Object> properties) {
		this.mdc = properties;
	}
	/**
	 * @return the exceptionInformation
	 */
	public HashMap<String, Object> getExceptionInformation() {
		return exceptionInformation;
	}
	/**
	 * @param exceptionInformation the exceptionInformation to set
	 */
	public void setExceptionInformation(HashMap<String, Object> exceptionInformation) {
		this.exceptionInformation = exceptionInformation;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		if (null != message) {
			this.message = message;
		}
	}
	/**
	 * @return the locationInformation
	 */
	public HashMap<String, Object> getLocationInformation() {
		return locationInformation;
	}
	/**
	 * @param locationInformation the locationInformation to set
	 */
	public void setLocationInformation(HashMap<String, Object> locationInformation) {
		this.locationInformation = locationInformation;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		if (null != level) {
			this.level = level;
		}
	}
	/**
	 * @return the loggerNameString
	 */
	public String getLoggerName() {
		return loggerName;
	}
	/**
	 * @param loggerNameString the loggerNameString to set
	 */
	public void setLoggerName(String loggerName) {
		if (null != loggerName) {
			this.loggerName = loggerName;
		}
	}
	/**
	 * @return the threadName
	 */
	public String getThreadName() {
		return threadName;
	}
	/**
	 * @param threadName the threadName to set
	 */
	public void setThreadName(String threadName) {
		if (null != threadName) {
			this.threadName = threadName;
		}
	}
    
//    public static String getLocalHostname() {
//    	System.setProperty("java.net.preferIPv4Stack" , "true");
//    	String hostname;
//		try {
//			hostname = java.net.InetAddress.getLocalHost().getHostName();
//		} catch (UnknownHostException e) {
//			hostname = "unknown";
//		}
//		return hostname;
//    }
//    
//    public static String getUsername() {
//    	String username;
//    	try {
//    		username = System.getProperty("user.name").toLowerCase(); 
//    	} catch (NullPointerException e) {
//    		username = "unknown";
//    	}
//    	return username;
//    }
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(getTimestamp());
		strBuf.append(' ');
		strBuf.append(getNdc());
		strBuf.append(' ');
		strBuf.append(getMdc());
		strBuf.append(' ');
		strBuf.append(getExceptionInformation());
		strBuf.append(' ');
		strBuf.append(getMessage());
		strBuf.append(' ');
		strBuf.append(getLocationInformation());
		strBuf.append(' ');
		strBuf.append(getLevel());
		strBuf.append(' ');
		strBuf.append(getLoggerName());
		strBuf.append(' ');
		strBuf.append(getThreadName());
		strBuf.append("\n");
		return strBuf.toString();
	}

}
