/**
 * 
 */
package com.stuartwarren.logit.logstash.v1;

import com.stuartwarren.logit.layout.ILayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public final class LogstashV1Layout extends LayoutFactory implements ILayout {
    
    private String logstashVersion = "1";

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#configure()
     */
    @Override
    public void configure() {
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#format(com.stuartwarren.logit.layout.Log)
     */
    public String format(Log log) {
        LogstashV1Log l = (LogstashV1Log) log;
        l.setVersion(this.logstashVersion);
        String stringLog = l.toString();
        return stringLog;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#getLog()
     */
    @Override
    public Log getLog() {
        return new LogstashV1Log();
    }

}
