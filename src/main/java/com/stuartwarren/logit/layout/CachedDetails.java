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
 * Enum Singleton to fetch expensive details only once on first use.
 *
 */
public enum CachedDetails {
    
    INSTANCE;
    private final String hostname;
    private final String username;
    private final String version;
    
    private CachedDetails() {
        String tempHostname;
        String tempUsername;
        
        LogitLog.debug("Trying to resolve hostname. May take some time...");
        System.setProperty("java.net.preferIPv4Stack" , "true");
        try {
            tempHostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LogitLog.warn("Unable to resolve [hostname]. Setting default.", e);
            tempHostname = "unknown";
        }
        hostname = tempHostname;
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [hostname] to [" + hostname + "].");
        }
        
        try {
            tempUsername = System.getProperty("user.name").toLowerCase();
        } catch (NullPointerException e) {
            LogitLog.warn("Unable to resolve [username]. Setting default.", e);
            tempUsername = "unknown";
        }
        username = tempUsername;
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [username] to [" + username + "].");
        }
        
        version = this.fetchVersion();
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Setting property [version] to [" + version + "].");
        }
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

}
