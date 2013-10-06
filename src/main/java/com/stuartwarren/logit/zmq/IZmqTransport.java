/**
 * 
 */
package com.stuartwarren.logit.zmq;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public interface IZmqTransport {
    
    /**
     * @return the endpoints
     */
    public String getEndpoints();

    /**
     * @param endpoints
     *            the endpoints to set
     */
    public void setEndpoints(String endpoints);

    /**
     * @return the socketType
     */
    public String getSocketType();

    /**
     * @param socketType
     *            the socketType to set
     */
    public void setSocketType(String socketType);

    /**
     * @return the linger
     */
    public int getLinger();

    /**
     * @param linger
     *            the linger to set
     */
    public void setLinger(int linger);

    /**
     * @return the bindConnect
     */
    public String getBindConnect();

    /**
     * @param bindConnect
     *            Whether to bind or connect to a port
     */
    public void setBindConnect(String bindConnect);

    /**
     * @return the sendHWM
     */
    public int getSendHWM();

    /**
     * @param sendHWM
     *            the sendHWM to set
     */
    public void setSendHWM(int sendHWM);

}
