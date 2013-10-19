/**
 * 
 */
package com.stuartwarren.test_logit.logback;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Logit {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("logit.debug", "");
        System.out.println("First instruction of Program....");
        Logger logger = LoggerFactory.getLogger(Logit.class);
        MDC.put("tags", "other");
        logger.error("There's been an error", new NullPointerException("Fake error thrown"));
        MDC.clear();
        System.exit(0);
    }

}