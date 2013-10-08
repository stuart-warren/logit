/**
 * 
 */
package com.stuartwarren.logit.jul;

import java.util.logging.LogManager;


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
                LogManager.getLogManager().reset();
            }
        });
    }
}
