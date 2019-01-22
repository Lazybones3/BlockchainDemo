package com.learning.wsdemo;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyServer extends WebSocketServer {

    private int port;

    public MyServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("服务端_" + port + "_打开连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("服务端_" + port + "_关闭连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("服务端_" + port + "_收到消息: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("服务端_" + port + "_发生错误");
    }

    @Override
    public void onStart() {
        System.out.println("服务端_" + port + "_启动成功");
    }

    //启动服务端
    public void startServer() {
        new Thread(this).start();
    }
}
