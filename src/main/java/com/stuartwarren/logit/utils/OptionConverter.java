/**
 * 
 */
package com.stuartwarren.logit.utils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// Contributors:   Avy Sharell (sharell@online.fr)
//                 Matthieu Verbert (mve@zurich.ibm.com)
//                 Colin Sampaleanu

/**
   A convenience class to convert property values to specific types.

   @author Ceki G&uuml;lc&uuml;
   @author Simon Kitching;
   @author Anders Kristensen
*/
public class OptionConverter {

  static String DELIM_START = "${";
  static char   DELIM_STOP  = '}';
  static int DELIM_START_LEN = 2;
  static int DELIM_STOP_LEN  = 1;

  /** OptionConverter is a static class. */
  private OptionConverter() {}

  /**
     Very similar to <code>System.getProperty</code> except
     that the {@link SecurityException} is hidden.

     @param key The key to search for.
     @param def The default value to return.
     @return the string value of the system property, or the default
     value if there is no property with that key.

     @since 1.1 */
  public
  static
  String getSystemProperty(String key, String def) {
    try {
      return System.getProperty(key, def);
    } catch(Throwable e) { // MS-Java throws com.ms.security.SecurityExceptionEx
      LogitLog.debug("Was not allowed to read system property \""+key+"\".");
      return def;
    }
  }



  /**
     If <code>value</code> is "true", then <code>true</code> is
     returned. If <code>value</code> is "false", then
     <code>true</code> is returned. Otherwise, <code>default</code> is
     returned.

     <p>Case of value is unimportant.  */
  public
  static
  boolean toBoolean(String value, boolean dEfault) {
    if(value == null)
      return dEfault;
    String trimmedVal = value.trim();
    if("true".equalsIgnoreCase(trimmedVal))
      return true;
    if("false".equalsIgnoreCase(trimmedVal))
      return false;
    return dEfault;
  }

}