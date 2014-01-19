/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author Stuart Warren 
 * @date 19 Jan 2014
 * <br/>
 * Approximate microsecond resolution timestamp strings in UTC ISO8601 format.<br/>
 * Use enum singleton to get time of first log in microseconds.<br/>
 * All future logs last 3 digits are based off the difference to this time so 
 * can only be relied on to assist ordering of muliple logs from the same millisecond.
 *
 */
public enum MicroTimestamp {
    
    INSTANCE ;
    
    private long startNanosecs;
    
    private static final TimeZone       UTC        = TimeZone.getTimeZone("UTC");
    private static final FastDateFormat DATEFORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS", UTC);
    
    private MicroTimestamp() {
        this.startNanosecs = System.nanoTime();
    }
    
    public String get() {
        return get(System.currentTimeMillis());
    }
    
    public String get(long timestamp) {  
       long microSeconds = (System.nanoTime() - this.startNanosecs) / 1000 ;
       long date = timestamp + (microSeconds/1000) ;
       // append microseconds & a literal Z for timezone information
       return DATEFORMAT.format(date) + String.format("%03dZ", microSeconds % 1000) ;
    }

}
