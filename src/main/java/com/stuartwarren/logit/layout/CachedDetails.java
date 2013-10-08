/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.net.UnknownHostException;

/**
 * @author Stuart Warren 
 * @date 8 Oct 2013
 *
 */
public class CachedDetails {
    
    private static volatile CachedDetails uniqueInstance;
    private String hostname;
    private String username;
    
    private CachedDetails() {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        try {
            this.setHostname(java.net.InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            this.setHostname("unknown");
        }
        try {
            this.setUsername(System.getProperty("user.name").toLowerCase());
        } catch (NullPointerException e) {
            this.setUsername("unknown");
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
    private void setHostname(String hostname) {
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
    private void setUsername(String username) {
        this.username = username;
    }
    

}
