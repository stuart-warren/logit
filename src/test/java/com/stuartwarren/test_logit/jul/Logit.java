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
        System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
        
        Logger logger = Logger.getLogger(Logit.class.getName());
        logger.log(Level.WARNING, "There's been an error", new NullPointerException("Fake error thrown"));
        System.exit(0);

    }

}
