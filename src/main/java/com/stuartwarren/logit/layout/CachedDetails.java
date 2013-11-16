/**
 * 
 */
package com.stuartwarren.logit.layout;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;

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
    private String version;
    
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
        this.setVersion(this.fetchVersion());
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [version] to [" + version + "].");
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
    
    private String fetchVersion() {
        // try to load from properties file
        String version = null;
        try {
            Properties p = new Properties();
            InputStream is = CachedDetails.class.getClassLoader().getResourceAsStream("version.properties");
            LogitLog.debug("Loading properties file.");
            if (is != null) {
                p.load(is);
                LogitLog.debug("Loaded properties file.");
                version = p.getProperty("logit.version");
            }
        } catch (Exception e) {
            LogitLog.debug("Error loading properties", e);        
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "";
            LogitLog.debug("Get empty string.");
        }
        
        return version;
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

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    private void setVersion(final String version) {
        this.version = version;
    }
    

}
