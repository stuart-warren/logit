/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.util.Map;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Log {
    
    private long                    timestamp;
    private String                  ndc;
    private Map<String, Object>     mdc;
    private ExceptionInformation           exceptionInformation;
    private LocationInformation            locationInformation;
    private String                  level;
    private int                     level_int;
    private String                  loggerName;
    private String                  threadName;
    private String                  message;

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the ndc
     */
    public String getNdc() {
        return ndc;
    }

    /**
     * @param ndc
     *            the ndc to set
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
     * @param properties
     *            the mdc to set
     */
    public void setMdc(Map<String, Object> properties) {
        this.mdc = properties;
    }

    /**
     * @return the exceptionInformation
     */
    public ExceptionInformation getExceptionInformation() {
        return exceptionInformation;
    }

    /**
     * @param exceptionInformation
     *            the exceptionInformation to set
     */
    public void setExceptionInformation(
            ExceptionInformation exceptionInformation) {
        this.exceptionInformation = exceptionInformation;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        if (null != message) {
            this.message = message;
        }
    }

    /**
     * @return the locationInformation
     */
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    /**
     * @param locationInformation
     *            the locationInformation to set
     */
    public void setLocationInformation(
            LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(String level) {
        if (null != level) {
            this.level = level;
        }
    }

    /**
     * @return the level_int
     */
    public int getLevel_int() {
        return level_int;
    }

    /**
     * @param level_int the level_int to set
     */
    public void setLevel_int(int level_int) {
        this.level_int = level_int;
    }

    /**
     * @return the loggerNameString
     */
    public String getLoggerName() {
        return loggerName;
    }

    /**
     * @param loggerNameString
     *            the loggerNameString to set
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
     * @param threadName
     *            the threadName to set
     */
    public void setThreadName(String threadName) {
        if (null != threadName) {
            this.threadName = threadName;
        }
    }

    // public static String getLocalHostname() {
    // System.setProperty("java.net.preferIPv4Stack" , "true");
    // String hostname;
    // try {
    // hostname = java.net.InetAddress.getLocalHost().getHostName();
    // } catch (UnknownHostException e) {
    // hostname = "unknown";
    // }
    // return hostname;
    // }
    //
    // public static String getUsername() {
    // String username;
    // try {
    // username = System.getProperty("user.name").toLowerCase();
    // } catch (NullPointerException e) {
    // username = "unknown";
    // }
    // return username;
    // }
    
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
        return strBuf.toString();
    }

}
