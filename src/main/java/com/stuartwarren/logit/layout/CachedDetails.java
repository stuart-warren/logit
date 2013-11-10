/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.net.UnknownHostException;

import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren 
 * @date 8 Oct 2013
 *
 */
public final class CachedDetails {
    
    private static volatile CachedDetails uniqueInstance;
    private String hostname;
    private String username;
    
    private CachedDetails() {
        LogitLog.debug("Trying to resolve hostname. May take some time...");
        System.setProperty("java.net.preferIPv4Stack" , "true");
        try {
            this.setHostname(java.net.InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            LogitLog.warn("Unable to resolve [hostname]. Setting default.", e);
            this.setHostname("unknown");
        }
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [hostname] to [" + hostname + "].");
        }
        try {
            this.setUsername(System.getProperty("user.name").toLowerCase());
        } catch (NullPointerException e) {
            LogitLog.warn("Unable to resolve [username]. Setting default.", e);
            this.setUsername("unknown");
        }
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [username] to [" + username + "].");
        }
    }
    
    public static CachedDetails getInstance() {
        if (uniqueInstance == null) {
            synchronized (CachedDetails.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CachedDetails();
                }
            }
        }
        return uniqueInstance;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    private void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    private void setUsername(final String username) {
        this.username = username;
    }
    

}
