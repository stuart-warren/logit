/**
 * 
 */
package com.stuartwarren.logit.tomcatvalve;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    private String layoutType = "log";
    private Log log;
    private LayoutFactory layoutFactory;
    private LayoutFactory layout;
    
    private String detailThreshold = "ERROR"; //not actually used...

    private String fields;
    private String tags;
    private List<String> iheaders;
    private List<String> oheaders;
    private List<String> cookies;
    
    
    public Layout() {
        LogitLog.debug("TomcatValve layout in use.");
        layoutFactory = new LayoutFactory();
        activateOptions();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.spi.OptionHandler#activateOptions()
     */
    public void activateOptions() {
        LogitLog.debug("Initialise Logfactory.");
        this.layout = layoutFactory.createLayout(this.layoutType);
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
     */
    public String format(Request request, Response response, long time) {
        this.log = doFormat(request, response, time);
        String stringLog = this.layout.format(this.log);
        return stringLog;
    }
    
    private Log doFormat(Request request, Response response, long time) {
        Log log = this.layout.getLog();
        Map<String,Object> httpFields = new HashMap<String,Object>();
        String level = "INFO";
        StringBuffer message = new StringBuffer();
        int status = response.getStatus();
        
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
        httpFields.put("response_size", response.getBytesWritten(false));
        httpFields.put("response_duration", time);
        if (status >= 400) {
            level = "ERROR";
        }
        if (status >= 500) {
            level = "CRITICAL";
        }
        
        if (null != this.iheaders && !this.iheaders.isEmpty()) {
            Map<String,Object> iheaders = new HashMap<String,Object>();
            for (String header : this.iheaders) {
                if (!"".equals(header)) {
                    iheaders.put(header, request.getHeader(header));
                }
            }
            httpFields.put("request_headers", iheaders);
        }
        if (null != this.oheaders && !this.oheaders.isEmpty()) {
            Map<String,Object> oheaders = new HashMap<String,Object>();
            for (String header : this.oheaders) {
                if (!"".equals(header)) {
                    oheaders.put(header, request.getHeader(header));
                }
            }
            httpFields.put("response_headers", oheaders);
        }
        if (null != this.cookies && !this.cookies.isEmpty()) {
            Map<String,Object> cookies = new HashMap<String,Object>();
            for (String cookie : this.cookies) {
                if (!"".equals(cookie)) {
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
    public void setLayoutType(String layoutType) {
        LogitLog.debug("Setting property [layoutType] to [" + layoutType + "].");
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
    public void setDetailThreshold(String detailThreshold) {
        LogitLog.debug("Setting property [detailThreshold] to [" + detailThreshold + "].");
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
    public void setFields(String fields) {
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
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @param headers
     */
    public void setOHeaders(String oheaders) {
        this.oheaders = Arrays.asList(oheaders.split("\\s*,\\s*"));
    }
    
    /**
     * @param iheaders
     */
    public void setIHeaders(String iheaders) {
        this.iheaders = Arrays.asList(iheaders.split("\\s*,\\s*"));
    }
    
    /**
     * @param cookies
     */
    public void setCookies(String cookies) {
        this.cookies = Arrays.asList(cookies.split("\\s*,\\s*"));
    }
    
    public static enum TCF implements IFieldName {
        
        HTTP("http");
        
        private String text;
        
        TCF(String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
    }

}
