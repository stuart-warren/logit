/**
 * 
 */
package com.stuartwarren.logit.logback;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;


/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 * 
 * Log4j1 Shutdown hook
 */
public class ShutdownHook {
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                loggerContext.stop();
            }
        });
    }
}
