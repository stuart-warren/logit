/**
 * 
 */
package com.stuartwarren.logit.utils;

/**
 * @modifiedby Stuart Warren 
 * @date 12 Oct 2013
 * 
 * Blatant rip off of Log4js LogLog
 *
 */
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

/**
   This class used to output log statements from within the log4j package.

   <p>Logit components cannot make logit logging calls. However, it is
   sometimes useful for the user to learn about what logit is
   doing. You can enable logit internal logging by defining the
   <b>logit.debug</b> variable.

   <p>All logit internal debug calls go to <code>System.out</code>
   where as internal error messages are sent to
   <code>System.err</code>. All internal messages are prepended with
   the string "logit: ".
   
   @since 0.8.2
   @author Ceki G&uuml;lc&uuml;
*/
public class LogitLog {

  /**
     Defining this value makes logit print logit-internal debug
     statements to <code>System.out</code>.
     
    <p> The value of this string is <b>logit.debug</b>.
    
    <p>Note that the search for all option names is case sensitive.  */
  public static final String DEBUG_KEY="logit.debug";
  protected static boolean debugEnabled = false;  

  /**
     In quietMode not even errors generate any output.
   */
  private static boolean quietMode = false;

  private static final String PREFIX = "logit: ";
  private static final String ERR_PREFIX = "logit:ERROR ";
  private static final String WARN_PREFIX = "logit:WARN ";

  static {
    String key = OptionConverter.getSystemProperty(DEBUG_KEY, null);

    if(key != null) { 
      debugEnabled = OptionConverter.toBoolean(key, true);
    }
  }

  /**
     Allows to enable/disable logit internal logging.
   */
  static
  public
  void setInternalDebugging(boolean enabled) {
    debugEnabled = enabled;
  }
  
  static
  public
  boolean isDebugEnabled() {
      return debugEnabled;
  }

  /**
     This method is used to output logit internal debug
     statements. Output goes to <code>System.out</code>.
  */
  public
  static
  void debug(String msg) {
    if(debugEnabled && !quietMode) {
      System.out.println(PREFIX+msg);
    }
  }

  /**
     This method is used to output logit internal debug
     statements. Output goes to <code>System.out</code>.
  */
  public
  static
  void debug(String msg, Throwable t) {
    if(debugEnabled && !quietMode) {
      System.out.println(PREFIX+msg);
      if(t != null) {
        t.printStackTrace(System.out);
      }
    }
  }
  

  /**
     This method is used to output logit internal error
     statements. There is no way to disable error statements.
     Output goes to <code>System.err</code>.
  */
  public
  static
  void error(String msg) {
    if(quietMode)
      return;
    System.err.println(ERR_PREFIX+msg);
  }  

  /**
     This method is used to output logit internal error
     statements. There is no way to disable error statements.
     Output goes to <code>System.err</code>.  
  */
  public
  static
  void error(String msg, Throwable t) {
    if(quietMode)
      return;

    System.err.println(ERR_PREFIX+msg);
    if(t != null) {
      t.printStackTrace();
    }
  }  

  /**
     In quite mode LogitLog generates strictly no output, not even
     for errors. 

     @param quietMode A true for not
  */
  public
  static
  void setQuietMode(boolean quietMode) {
    LogitLog.quietMode = quietMode;
  }

  /**
     This method is used to output logit internal warning
     statements. There is no way to disable warning statements.
     Output goes to <code>System.err</code>.  */
  public
  static
  void warn(String msg) {
    if(quietMode)
      return;

    System.err.println(WARN_PREFIX+msg);
  }  

  /**
     This method is used to output logit internal warnings. There is
     no way to disable warning statements.  Output goes to
     <code>System.err</code>.  */
  public
  static
  void warn(String msg, Throwable t) {
    if(quietMode)
      return;

    System.err.println(WARN_PREFIX+msg);
    if(t != null) {
      t.printStackTrace();
    }
  }  
}