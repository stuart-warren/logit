package com.stuartwarren.test_logit.log4j1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.stuartwarren.logit.fields.HttpField;
import com.stuartwarren.logit.fields.HttpField.HTTP;

public class Logit {

    public static void main(String[] args) {
        System.setProperty("logit.trace", "");
        Logger logger = Logger.getLogger(Logit.class.getName());
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
        logger.info("non exception");
        System.exit(0);
        LogManager.shutdown();
    }

}
