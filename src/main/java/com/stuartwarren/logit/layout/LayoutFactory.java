/**
 * 
 */
package com.stuartwarren.logit.layout;

import com.stuartwarren.logit.gelf.v1.GelfV1Layout;
import com.stuartwarren.logit.logstash.v0.LogstashV0Layout;
import com.stuartwarren.logit.logstash.v1.LogstashV1Layout;
import com.stuartwarren.logit.utils.LogitLog;

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
            LogitLog.debug("Logstashv1 layout in use.");
            this.layout = new LogstashV1Layout();
        } else if (layoutType.equalsIgnoreCase("logstashv0")) {
            LogitLog.debug("Logstashv0 layout in use.");
            this.layout = new LogstashV0Layout();
        } else if (layoutType.equalsIgnoreCase("gelfv1")) {
            LogitLog.debug("Gelf1 layout in use.");
            this.layout = new GelfV1Layout();
        } else {
            LogitLog.debug("Default Layout [Logstashv1] in use.");
            this.layout = new LogstashV1Layout();
        }
        return this.layout;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#getLog()
     */
    public Log getLog() {
        return this.layout.getLog();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#configure()
     */
    public void configure() {
        this.layout.configure();
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#format(com.stuartwarren.logit.layout.Log)
     */
    public String format(Log log) {
        String stringLog = this.layout.format(log);
        return stringLog;
    }

}
