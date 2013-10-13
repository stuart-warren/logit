package com.stuartwarren.test_logit.log4j1;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class Logit {

    public static void main(String[] args) {
        System.setProperty("logit.debug", "");
        Logger logger = Logger.getLogger(Logit.class.getName());
        MDC.put("stuff", "other");
        logger.error("There's been an error", new NullPointerException("Fake error thrown"));
        System.exit(0);
        LogManager.shutdown();
    }

}
