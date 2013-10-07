package com.stuartwarren.test_logit.log4j1;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class LogIt {

    public static void main(String[] args) {
        System.setProperty("log4j.debug", "");
        Logger logger = Logger.getLogger(LogIt.class.getName());
//        System.out.println("First instruction of Program....");
//        int n = 0;
//        while (n < 10) {
//            n = n + 1;
//            MDC.put("test", "yeah");
//            logger.info("Hi there " + n);
//            MDC.clear();
//        }
//        System.out.println("Last instruction of Program....");
//
//        /*
//         * Need to exit the application or shutdown the log manager to close ZMQ
//         * sockets cleanly.
//         */
//        LogManager.shutdown();
//        // System.exit(0);
        
        MDC.put("stuff", "other");
        logger.error("There's been an error", new NullPointerException("Fake error thrown"));
        System.exit(0);
        LogManager.shutdown();
    }

}
