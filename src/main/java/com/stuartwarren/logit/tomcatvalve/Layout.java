/**
 * 
 */
package com.stuartwarren.logit.tomcatvalve;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
//import org.apache.catalina.valves.AccessLogValve;







import com.stuartwarren.logit.fields.IFieldName;
import com.stuartwarren.logit.layout.IFrameworkLayout;
import com.stuartwarren.logit.layout.LayoutFactory;
import com.stuartwarren.logit.layout.Log;
import com.stuartwarren.logit.utils.LogitLog;

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
    private String tags;
    private transient List<String> iheaders;
    private transient List<String> oheaders;
    private transient List<String> cookies;
    
    
    public Layout() {
        LogitLog.debug("TomcatValve layout in use.");
        layoutFactory = new LayoutFactory();
        activateOptions();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.spi.OptionHandler#activateOptions()
     */
    public final void activateOptions() {
        LogitLog.debug("Initialise Logfactory.");
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
        final Map<String,Object> httpFields = new ConcurrentHashMap<String,Object>();
        String level = "INFO";
        final StringBuffer message = new StringBuffer();
        final int status = response.getStatus();
        
        log.setTimestamp(request.getCoyoteRequest().getStartTime());
        httpFields.put("request_parameters", request.getParameterMap());
        httpFields.put("remote_host", request.getRemoteHost());
        httpFields.put("remote_user", request.getRemoteUser());
        httpFields.put("request_method", request.getMethod());
        message.append(request.getMethod());
        message.append(' ');
        httpFields.put("request_uri", request.getRequestURI());
        message.append(request.getRequestURI());
        message.append('?');
        httpFields.put("request_querystring", request.getQueryString());
        message.append(request.getQueryString());
        message.append(' ');
        httpFields.put("request_protocol", request.getProtocol());
        message.append(request.getProtocol());
        message.append(' ');
        httpFields.put("response_status", status);
        message.append(status);
        final Session session = request.getSessionInternal(false);
        if (session != null) {
            httpFields.put("session_id", session.getIdInternal());
        }
        httpFields.put("response_size", response.getBytesWritten(false));
        httpFields.put("server_name", request.getServerName());
        httpFields.put("response_duration", time);
        if (status >= HTTP_400) {
            level = "ERROR";
        }
        if (status >= HTTP_500) {
            level = "CRITICAL";
        }
        
        if (null != this.iheaders && !this.iheaders.isEmpty()) {
            final Map<String,Object> iheaders = new ConcurrentHashMap<String,Object>();
            for (final String header : this.iheaders) {
                if (!EMPTY_STRING.equals(header)) {
                    iheaders.put(header, request.getHeader(header));
                }
            }
            httpFields.put("request_headers", iheaders);
        }
        if (null != this.oheaders && !this.oheaders.isEmpty()) {
            final Map<String,Object> oheaders = new ConcurrentHashMap<String,Object>();
            for (final String header : this.oheaders) {
                if (!EMPTY_STRING.equals(header)) {
                    oheaders.put(header, request.getHeader(header));
                }
            }
            httpFields.put("response_headers", oheaders);
        }
        if (null != this.cookies && !this.cookies.isEmpty()) {
            final Map<String,Object> cookies = new ConcurrentHashMap<String,Object>();
            for (final String cookie : this.cookies) {
                if (!EMPTY_STRING.equals(cookie)) {
                    cookies.put(cookie, request.getHeader(cookie));
                }
            }
            httpFields.put("cookies", cookies);
        }
        
        log.setLevel(level);
        log.setTags(tags);
        log.setFields(fields);
        log.setMessage(message.toString());
        log.addField(TCF.HTTP, httpFields);
        log.appendTag("valve");
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
            LogitLog.debug("Setting property [layoutType] to [" + layoutType + "].");
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
            LogitLog.debug("Setting property [detailThreshold] to [" + detailThreshold + "].");
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
    
    public static enum TCF implements IFieldName {
        
        HTTP("http");
        
        private String text;
        
        TCF(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
    }

}
