/**
 * 
 */
package com.stuartwarren.logit.appender;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public interface IAppender {
    
    /**
     * The logging framework should run this method
     * prior to sending logs.
     */
    public void configure();
    
    /**
     * The logging framework should call this to quickly
     * close and sockets/files/connections at the end of
     * execution or by a shutdownhook.
     */
    public void stop();
    
    /**
     * The actual append method. Send/write your data here.
     * @param log - String having been already formatted
     * by the chosen layout.
     */
    public void appendString(String log);

}
