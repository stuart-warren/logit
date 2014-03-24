/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author Stuart Warren 
 * @date 24 Mar 2014
 *
 */
public class Timestamp {

    private long                         timestamp;

    private static final TimeZone       THISTZ   = TimeZone.getDefault();
    private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = 
            FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZ", THISTZ);

    private String dateFormat(long timestamp) {
        return ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(this.timestamp);
    }

    public Timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public Timestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return this.dateFormat(this.timestamp);
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

}
