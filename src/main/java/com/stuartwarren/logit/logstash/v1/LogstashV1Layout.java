/**
 * 
 */
package com.stuartwarren.logit.logstash.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stuartwarren.logit.layout.ILayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public final class LogstashV1Layout extends LayoutFactory implements ILayout {
    
    private final static String LOGSTASH_VERSION = "1";

    private ObjectMapper objectMapper = new ObjectMapper();

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#configure()
     */
    @Override
    public void configure() {
        // empty
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#format(com.stuartwarren.logit.layout.Log)
     */
    public String format(final Log log) {
        final LogstashV1Log l = (LogstashV1Log) log;
        l.setVersion(LOGSTASH_VERSION);
        return l.toString();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#getLog()
     */
    @Override
    public Log getLog() {
        return new LogstashV1Log(objectMapper);
    }

}
