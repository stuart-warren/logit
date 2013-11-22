/**
 * 
 */
package com.stuartwarren.logit.zmq;

import org.zeromq.ZMQ;

/**
 * @author Stuart Warren
 * @date 29 Sep 2013
 */
public enum SocketType {

    PUSHPULL(ZMQ.PUSH, ZMQ.PULL, "pushpull"), 
    PUBSUB(ZMQ.PUB, ZMQ.SUB, "pubsub");

    private final int    serverSocket;
    private final int    clientSocket;
    private final String name;

    SocketType(final int clientSocket, final int serverSocket, final String name) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static int getServerSocket(final String name) {
        int serverSocket = ZMQ.PUSH;
        for (final SocketType s : SocketType.values()) {
            if (name.equals(s.name)) {
                serverSocket = s.serverSocket;
            }
        }
        return serverSocket;
    }

    public static int getClientSocket(final String name) {
        int clientSocket = ZMQ.PULL;
        for (final SocketType s : SocketType.values()) {
            if (name.equals(s.name)) {
                clientSocket = s.clientSocket;
            }
        }
        return clientSocket;
    }

    public static boolean isValidType(final String name) {
        boolean result = false;
        for (final SocketType s : SocketType.values()) {
            if (name.equalsIgnoreCase(s.name)) {
                result = true;
            }
        }
        return result;
    }
}
