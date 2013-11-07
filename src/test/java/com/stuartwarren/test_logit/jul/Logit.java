/**
 * 
 */
package com.stuartwarren.test_logit.jul;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stuart Warren 
 * @date 8 Oct 2013
 *
 */
public class Logit {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("logit.debug", "");
        System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
        
        Logger logger = Logger.getLogger(Logit.class.getName());
        logger.log(Level.WARNING, "There's been an error", new NullPointerException("Fake error thrown"));
        logger.info("non exception");
        for(int x = 0; x < 1000000; x++) {
            logger.info("Logging message: " + x);
        }
        System.exit(0);

    }

}
