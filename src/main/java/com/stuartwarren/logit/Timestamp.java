/**
 * 
 */
package com.stuartwarren.logit;

import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public class Timestamp {

    public long                         timestamp;

    private static final TimeZone       thisTZ   = TimeZone.getDefault();
    private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = 
            FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZ", thisTZ);

    private String dateFormat(long timestamp) {
        return ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(timestamp);
    }

    public Timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public Timestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return this.dateFormat(this.timestamp);
    }

}
