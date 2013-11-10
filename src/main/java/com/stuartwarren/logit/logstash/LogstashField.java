/**
 * 
 */
package com.stuartwarren.logit.logstash;

import com.stuartwarren.logit.fields.IFieldName;

/**
 * @author Stuart Warren 
 * @date 4 Nov 2013
 *
 */
public class LogstashField {
    
    public static enum LOGSTASH implements IFieldName {
        
        /**
         * MESSAGE - @message
         * Original log message - Logstash v0 only
         */
        MESSAGE("@message"),
        /**
         * FIELDS - @fields
         * Custom fields added inside fields object - Logstash v0 only
         */
        FIELDS("@fields"),
        /**
         * TAGS - @tags
         * List of strings to assist with filtering - Logstash v0 only
         */
        TAGS("@tags"),
        /**
         * TIMESTAMP - @timestamp
         * Time event occured, in ISO8601 format
         */
        TIMESTAMP("@timestamp"),
        /**
         * VERSION - @version
         * Version of logstash JSON spec
         */
        VERSION("@version")
        ;
        
        private String text;
        
        LOGSTASH(String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
