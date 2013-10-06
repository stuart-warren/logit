/**
 * 
 */
package com.stuartwarren.logit.logback;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.jeromq.ZMQ;

/**
 * @author Stuart Warren 
 * @date 6 Oct 2013
 *
 */
public class ZmqOutputStream extends OutputStream {

    private final ZMQ.Socket socket;
    
    public ZmqOutputStream(ZMQ.Socket socket) {
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
        socket.send(bytes, 0);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

}
