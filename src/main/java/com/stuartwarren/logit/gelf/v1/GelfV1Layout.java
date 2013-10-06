/**
 * 
 */
package com.stuartwarren.logit.gelf.v1;

import com.stuartwarren.logit.layout.ILayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public final class GelfV1Layout extends LayoutFactory implements ILayout {
    
    private String gelfVersion = "1.0";

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
        GelfV1Log l = (GelfV1Log) log;
        l.setVersion(this.gelfVersion);
        String stringLog = l.toString();
        return stringLog;
    }

    /* (non-Javadoc)
     * @see com.stuartwarren.logit.layout.ILayout#getLog()
     */
    @Override
    public Log getLog() {
        return new GelfV1Log();
    }

}
