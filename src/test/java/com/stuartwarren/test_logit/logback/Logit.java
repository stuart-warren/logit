/**
 * 
 */
package com.stuartwarren.test_logit.logback;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.stuartwarren.logit.fields.HttpField;
import com.stuartwarren.logit.fields.HttpField.HTTP;

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
        System.setProperty("logit.trace", "");
        System.out.println("First instruction of Program....");
        Logger logger = LoggerFactory.getLogger(Logit.class);
        MDC.put("stuff", "other");
        List<String> myList = new ArrayList<String>();
        myList.add("item1");
        myList.add("item2");
        Map<String,String> myMap = new HashMap<String,String>();
        myMap.put("k1", "v1");
        myMap.put("k2", "v2");
        // Using an existing Field object just to test...
        HttpField.put(HTTP.RESPONSE_HEADERS, myList);
        HttpField.put(HTTP.REQUEST_HEADERS, myMap);
        logger.error("There's been an error", new NullPointerException("Fake error thrown"));
        HttpField.clear();
        MDC.clear();
        logger.info("test no exception");
        System.exit(0);
    }

}