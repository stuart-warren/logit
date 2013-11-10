/**
 * 
 */
package com.stuartwarren.logit.logstash.v0;

import com.stuartwarren.logit.layout.ILayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public final class LogstashV0Layout extends LayoutFactory implements ILayout {

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
        final LogstashV0Log l = (LogstashV0Log) log;
        return l.toString();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#getLog()
     */
    @Override
    public Log getLog() {
        return new LogstashV0Log();
    }

}
