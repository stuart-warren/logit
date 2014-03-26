/**
 * 
 */
package com.stuartwarren.logit.tomcatvalve;

import com.stuartwarren.logit.fields.HttpField;
import com.stuartwarren.logit.fields.HttpField.HTTP;
import com.stuartwarren.logit.layout.IFrameworkLayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.utils.LogitLog;

import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class Layout implements IFrameworkLayout {
    
    private String layoutType;
    private transient LayoutFactory layoutFactory;
    private transient LayoutFactory layout;
    
    private String detailThreshold = "ERROR"; //not actually used...
    private final static int HTTP_400 = 400;
    private final static int HTTP_500 = 500;
    private final static String EMPTY_STRING = "";

    private String fields;
    private Map<String, String> parsedFields;
    private String tags;
    private transient List<String> iheaders;
    private transient List<String> oheaders;
    private transient List<String> cookies;
    
    
    public Layout() {
        LogitLog.debug("TomcatValve layout in use.");
        layoutFactory = new LayoutFactory();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.spi.OptionHandler#activateOptions()
     */
    public final void activateOptions() {
        LogitLog.debug("TomcatValve Initialise Logfactory.");
        this.layout = layoutFactory.createLayout(this.layoutType);
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
     */
    public String format(final Request request, final Response response, final long time) {
        final Log log = doFormat(request, response, time);
        return this.layout.format(log);
    }
    
    private Log doFormat(final Request request, final Response response, final long time) {
        final Log log = this.layout.getLog();
        String level = "INFO";
        final StringBuffer message = new StringBuffer();
        final int status = response.getStatus();
        
        HttpField.put(HTTP.REQUEST_PARAMS, request.getParameterMap());
        HttpField.put(HTTP.REMOTE_HOST, request.getRemoteHost());
        HttpField.put(HTTP.REMOTE_USER, request.getRemoteUser());
        HttpField.put(HTTP.REQUEST_METHOD, request.getMethod());
        message.append(request.getMethod());
        message.append(' ');
        HttpField.put(HTTP.REQUEST_URI, request.getRequestURI());
        message.append(request.getRequestURI());
        message.append('?');
        HttpField.put(HTTP.REQUEST_QUERYSTRING, request.getQueryString());
        message.append(request.getQueryString());
        message.append(' ');
        HttpField.put(HTTP.REQUEST_PROTOCOL, request.getProtocol());
        message.append(request.getProtocol());
        message.append(' ');
        HttpField.put(HTTP.RESPONSE_STATUS, status);
        message.append(status);
        final Session session = request.getSessionInternal(false);
        if (session != null) {
            HttpField.put(HTTP.SESSION_ID, session.getIdInternal());
        }
        HttpField.put(HTTP.RESPONSE_SIZE, response.getBytesWritten(false));
        HttpField.put(HTTP.SERVER_NAME, request.getServerName());
        // Tomcat duration is in milliseconds, convert to seconds.
        HttpField.put(HTTP.RESPONSE_DURATION, (double) time/1000.0);
        if (status >= HTTP_400) {
            level = "ERROR";
        }
        if (status >= HTTP_500) {
            level = "CRITICAL";
        }
        
        if (null != this.iheaders && !this.iheaders.isEmpty()) {
            Map<String,Object> iheaders = new ConcurrentHashMap<String,Object>();
            for (String header : this.iheaders) {
                if (!EMPTY_STRING.equals(header)) {
                    String headerValue = request.getHeader(header);
                    if (null != headerValue) {
                        iheaders.put(header, headerValue);
                    }
                }
            }
            HttpField.put(HTTP.REQUEST_HEADERS, iheaders);
        }
        if (null != this.oheaders && !this.oheaders.isEmpty()) {
            Map<String,Object> oheaders = new ConcurrentHashMap<String,Object>();
            for (String header : this.oheaders) {
                if (!EMPTY_STRING.equals(header)) {
                    String headerValue = response.getHeader(header);
                    if (null != headerValue) {
                        oheaders.put(header, headerValue);
                    }
                }
            }
            HttpField.put(HTTP.RESPONSE_HEADERS, oheaders);
        }
        if (null != this.cookies && !this.cookies.isEmpty()) {
            Map<String,Object> cookies = new ConcurrentHashMap<String,Object>();
            for (String cookie : this.cookies) {
                if (!EMPTY_STRING.equals(cookie)) {
                    String cookieValue = request.getHeader(cookie);
                    if (null != cookieValue) {
                        cookies.put(cookie, cookieValue);
                    }
                }
            }
            HttpField.put(HTTP.COOKIES, cookies);
        }
        
        log.setTimestamp(request.getCoyoteRequest().getStartTime())
            .setLevel(level)
            .setTags(tags)
            .setFields(parsedFields)
            .setMessage(message.toString())
            .appendTag("valve")
            .addRegisteredFields();
        
        HttpField.clear();
        return log;
    }

    /**
     * @return the layoutType
     */
    public String getLayoutType() {
        return layoutType;
    }

    /**
     * @param layoutType the layoutType to set
     */
    public void setLayoutType(final String layoutType) {
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("TomcatValve Setting property [layoutType] to [" + layoutType + "].");
        }
        this.layoutType = layoutType;
    }

    /**
     * @return the detailThreshold
     */
    public String getDetailThreshold() {
        return detailThreshold;
    }

    /**
     * @param detailThreshold the detailThreshold to set
     */
    public void setDetailThreshold(final String detailThreshold) {
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("TomcatValve Setting property [detailThreshold] to [" + detailThreshold + "].");
        }
        this.detailThreshold = detailThreshold;
    }

    /**
     * @return the fields
     */
    public String getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(final String fields) {
        this.fields = fields;
        this.parsedFields = Log.parseFields(fields);
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(final String tags) {
        this.tags = tags;
    }

    /**
     * @param headers
     */
    public void setOHeaders(final String oheaders) {
        this.oheaders = Arrays.asList(oheaders.split("\\s*,\\s*"));
    }
    
    /**
     * @param iheaders
     */
    public void setIHeaders(final String iheaders) {
        this.iheaders = Arrays.asList(iheaders.split("\\s*,\\s*"));
    }
    
    /**
     * @param cookies
     */
    public void setCookies(final String cookies) {
        this.cookies = Arrays.asList(cookies.split("\\s*,\\s*"));
    }
    
}
