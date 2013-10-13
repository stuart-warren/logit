/**
 * 
 */
package com.stuartwarren.logit.zmq;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.jeromq.ZMQ;

import com.stuartwarren.logit.utils.LogitLog;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class ZmqOutputStream extends OutputStream {

    private final ZMQ.Socket socket;
    
    public ZmqOutputStream(ZMQ.Socket socket) {
        LogitLog.debug("Using output stream.");
        this.socket = socket;
    }
    
    @Override
    public void write(int i) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        socket.send(b.array(), 0);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if (LogitLog.isDebugEnabled()) {
            LogitLog.debug("Sending log: [" + new String(bytes) + "].");
        }
        socket.send(bytes, 0);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
