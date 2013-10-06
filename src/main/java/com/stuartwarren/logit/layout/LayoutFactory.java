/**
 * 
 */
package com.stuartwarren.logit.layout;

import com.stuartwarren.logit.gelf.v1.GelfV1Layout;
import com.stuartwarren.logit.logstash.v0.LogstashV0Layout;
import com.stuartwarren.logit.logstash.v1.LogstashV1Layout;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class LayoutFactory implements ILayout {
    
    LayoutFactory layout;
    
    public LayoutFactory() {
    }
    
    public LayoutFactory createLayout(String layoutType) {
        this.layout = null;
        if (layoutType.equalsIgnoreCase("logstashv1")) {
            this.layout = new LogstashV1Layout();
        } else if (layoutType.equalsIgnoreCase("logstashv0")) {
            this.layout = new LogstashV0Layout();
        } else if (layoutType.equalsIgnoreCase("gelfv1")) {
            this.layout = new GelfV1Layout();
        } else {
            this.layout = new LayoutFactory();
        }
        return this.layout;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#getLog()
     */
    @Override
    public Log getLog() {
        return this.getLog();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#configure()
     */
    @Override
    public void configure() {
        this.layout.configure();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#format(com.stuartwarren.logit.layout.Log)
     */
    @Override
    public String format(Log log) {
        String stringLog = this.layout.format(log);
        return stringLog;
    }

}
