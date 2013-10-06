/**
 * 
 */
package com.stuartwarren.logit.layout;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public interface ILayout {
    
    public Log getLog();
    
    public void configure();
    
    public String format(Log log);

}
