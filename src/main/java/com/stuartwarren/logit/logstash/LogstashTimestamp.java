/**
 * 
 */
package com.stuartwarren.logit.logstash;

import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import com.stuartwarren.logit.layout.Timestamp;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public class LogstashTimestamp extends Timestamp {

    private static final TimeZone       UTC                                           = TimeZone.getTimeZone("UTC");
    private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS_UTC = FastDateFormat.getInstance(
                                                                                          "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", UTC);

    private String dateFormat(final long timestamp) {
        return ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS_UTC.format(timestamp);
    }

    public LogstashTimestamp() {
        super();
    }

    /**
     * @param timestamp
     */
    public LogstashTimestamp(final Long timestamp) {
        super(timestamp);
    }

    public String toString() {
        return this.dateFormat(this.getTimestamp());
    }

}
