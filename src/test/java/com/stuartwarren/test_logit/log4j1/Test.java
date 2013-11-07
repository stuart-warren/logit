/**
 * 
 */
package com.stuartwarren.test_logit.log4j1;

import org.apache.log4j.Logger;

/**
 * @author Stuart Warren 
 * @date 26 Oct 2013
 * 
 *
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Test.class.getName());
        System.out.println(Test.class.getName());
        logger.info("first message");
        long start = System.currentTimeMillis();
        System.out.println("First message: " + start);
        for(int x = 0; x < 1000000; x++) {
            logger.info("Logging message: " + x);
        }
        long end = System.currentTimeMillis();
        System.out.println("Last message: " + end);
        long time = end - start;
        System.out.println("Time taken: " + time + "ms");
        System.exit(0);
    }

}
