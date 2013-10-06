/**
 * 
 */
package com.stuartwarren.logit.gelf.v1;

import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import com.stuartwarren.logit.layout.Timestamp;

/**
 * @author Stuart Warren
 * @date 22 Sep 2013
 */
public class GelfTimestamp extends Timestamp {

    private static final TimeZone       UTC                                           = TimeZone.getTimeZone("UTC");
    private static final FastDateFormat UNIX_MICROSECOND_DECIMAL_UTC = FastDateFormat.getInstance(
                                                                                          "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", UTC);

    private String dateFormat(long timestamp) {
        return UNIX_MICROSECOND_DECIMAL_UTC.format(timestamp);
    }

    public GelfTimestamp() {
        super();
    }

    /**
     * @param timestamp
     */
    public GelfTimestamp(Long timestamp) {
        super(timestamp);
    }

    public String toString() {
        return this.dateFormat(this.timestamp);
    }

}
