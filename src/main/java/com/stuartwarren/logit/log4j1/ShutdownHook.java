/**
 * 
 */
package com.stuartwarren.logit.log4j1;

import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.LogLog;

/**
 * @author Stuart Warren
 * @date 29 Sep 2013
 */
public class ShutdownHook {
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LogLog.debug("Inside ShutDown Hook");
                LogManager.shutdown();
            }
        });
        LogLog.debug("ShutDown Hook Attached.");
    }
}
