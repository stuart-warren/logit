/**
 * 
 */
package com.stuartwarren.logit.log4j1;

import org.apache.log4j.LogManager;

import com.stuartwarren.logit.utils.LogitLog;

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
                LogitLog.debug("Inside ShutDown Hook");
                LogManager.shutdown();
            }
        });
        LogitLog.debug("ShutDown Hook Attached.");
    }
}
