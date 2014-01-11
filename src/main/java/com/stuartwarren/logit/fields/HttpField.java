/**
 * 
 */
package com.stuartwarren.logit.fields;

/**
 * @author Stuart Warren 
 * @date 22 Nov 2013
 *
 */
public class HttpField extends Field {
    
    private static final HttpField FIELD = new HttpField();
    
    private HttpField() {
        this.setSection(ROOT.HTTP);
        Field.register(this);
    }
    
    public final static void put(final IFieldName key, final Object s) {
        FIELD.put0(key, s);
    }
    
    public static Object get(final IFieldName key) {
        return FIELD.get0(key);
    }
    
    public static void clear() {
        FIELD.clear0();
    }
    
    public String toString() {
        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(get(HTTP.REQUEST_METHOD));
        strBuf.append(' ');
        strBuf.append(get(HTTP.REQUEST_URI));
        strBuf.append('?');
        strBuf.append(get(HTTP.REQUEST_QUERYSTRING));
        strBuf.append(' ');
        strBuf.append(get(HTTP.REQUEST_PROTOCOL));
        strBuf.append(' ');
        strBuf.append(get(HTTP.RESPONSE_STATUS));
        return strBuf.toString();
    }
    
    public static enum HTTP implements IFieldName {
        /**
         * REQUEST_PARAMS - request_parameters<br/>
         * Parameters object, passed in from the URL query string.<br/>
         * Each key has a list of values<br/>
         * "request_parameters":{"param1":["value1"]}
         */
        REQUEST_PARAMS("request_parameters"),
        /**
         * REMOTE_HOST - remote_host<br/>
         * IP or resolved hostname of client.
         */
        REMOTE_HOST("remote_host"),
        /**
         * REMOTE_USER - remote_user<br/>
         * If authorisation used, this is logged in username.<br/>
         * May be wise to hash this field for privacy reasons.
         */
        REMOTE_USER("remote_user"),
        /**
         * REQUEST_METHOD - request_method<br/>
         * Verb used to make request. eg GET, POST.
         */
        REQUEST_METHOD("request_method"),
        /**
         * REQUEST_URI - request_uri<br/>
         * Path to document on server. eg /index.html.
         */
        REQUEST_URI("request_uri"),
        /**
         * REQUEST_QUERYSTRING - request_querystring<br/>
         * The whole query string of key=value etc
         */
        REQUEST_QUERYSTRING("request_querystring"),
        /**
         * REQUEST_PROTOCOL - request_protocol<br/>
         * Protocol used in the request. eg HTTP/1.1.
         */
        REQUEST_PROTOCOL("request_protocol"),
        /**
         * RESPONSE_STATUS - response_status<br/>
         * Response http statuscode returned to the client. 
         * eg 404, 500.
         */
        RESPONSE_STATUS("response_status"),
        /**
         * SESSION_ID - session_id<br/>
         * On servers that store sessions, this should be the session identifier.
         */
        SESSION_ID("session_id"),
        /**
         * RESPONSE_SIZE - response_size<br/>
         * Size of returned data to client in bytes
         */
        RESPONSE_SIZE("response_size"),
        /**
         * SERVER_NAME - server_name<br/>
         * The hostname or IP of the server used in the request.
         */
        SERVER_NAME("server_name"),
        /**
         * RESPONSE_DURATION - response_duration<br/>
         * Duration taken to serve the request
         */
        RESPONSE_DURATION("response_duration"),
        /**
         * REQUEST_HEADERS - request_headers<br/>
         * Map of headers sent in the request.<br/>
         * eg "request_headers":{"Referer":null,"User-Agent":"Mozilla/5.0 Chrome/30.0.1599.101 Safari/537.36"}
         */
        REQUEST_HEADERS("request_headers"),
        /**
         * RESPONSE_HEADERS - response_headers<br/>
         * Map of headers returned in the response.<br/>
         * eg "response_headers":{"content_encoding":"gzip","expires":"Fri, 01 Jan 1990 00:00:00 GMT"}
         */
        RESPONSE_HEADERS("response_headers"),
        /**
         * COOKIES - cookies<br/>
         * Map of cookies
         */
        COOKIES("cookies")
        ;
        
        private String text;
        
        HTTP(final String text) {
            this.text = text;
        }
        
        public String toString() {
            return this.text;
        }
        
    }

}
