/**
 * 
 */
package com.stuartwarren.logit.gelf.v1;

import com.stuartwarren.logit.fields.IFieldName;

/**
 * @author Stuart Warren 
 * @date 4 Nov 2013
 *
 */
public class GelfV1Field {
    
    public static enum GELF implements IFieldName {
        
        /**
         * HOST - host
         * host machine sending log
         */
        HOST("host"),
        /**
         * FACILITY - facility
         * type of log
         */
        FACILITY("facility"),
        /**
         * VERSION - version
         * Version of Gelf specification
         */
        VERSION("version"),
        /**
         * TIMESTAMP - timestamp
         * Time of event
         */
        TIMESTAMP("timestamp"),
        /**
         * SHORTMESSAGE - short_message
         * Original logged message
         */
        SHORTMESSAGE("short_message")
        ;
        
        private String text;
        
        GELF(String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
