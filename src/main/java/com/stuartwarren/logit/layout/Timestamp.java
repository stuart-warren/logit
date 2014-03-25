/**
 * 
 */
package com.stuartwarren.logit.layout;

import ch.qos.logback.core.util.CachingDateFormatter;

import java.util.TimeZone;

/**
 * @author Stuart Warren 
 * @date 24 Mar 2014
 *
 */
public class Timestamp {

    private long                         timestamp;

    static CachingDateFormatter cachingDateFormatter = new CachingDateFormatter("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    static {
        cachingDateFormatter.setTimeZone(TimeZone.getDefault());
    }

    private String dateFormat(long timestamp) {
        return cachingDateFormatter.format(timestamp);
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
