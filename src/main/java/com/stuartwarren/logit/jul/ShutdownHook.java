/**
 * 
 */
package com.stuartwarren.logit.jul;

import java.util.logging.LogManager;

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
                LogManager.getLogManager().reset();
            }
        });
        LogitLog.debug("ShutDown Hook Attached.");
    }
}
