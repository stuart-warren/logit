/**
 * 
 */
package com.stuartwarren.logit.utils;

/**
 * @author Stuart Warren 
 * @date 19 Oct 2013
 *
 */
public class LayoutLoader {
    
    /**
     * 
     * @param strClassName 
     *              Class specified by name (String)
     * @param defaultObj 
     *              Object to use incase specified class can't be found
     * @return
     *      Instance of Class requested
     */
    public static Object instantiateByClassName(String strClassName, Object defaultObj)
    {
        Object result;
        
        try
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(strClassName);
            result = clazz.newInstance();
        }
        catch (Exception ex)
        {
            result = defaultObj;
            LogitLog.error("Error parsing class name of Layout", ex);
        }
        return result;
    }

}
