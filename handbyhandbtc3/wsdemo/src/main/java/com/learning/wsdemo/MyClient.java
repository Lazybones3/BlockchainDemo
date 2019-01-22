package com.learning.wsdemo;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyClient extends WebSocketClient {
    private String name;

    public MyClient(URI serverUri, String name) {
        super(serverUri);
        this.name = name;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("客户端_" + name + "_打开连接");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("客户端_" + name + "_收到消息: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端_" + name + "_关闭连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端_" + name + "_发生错误");
    }
}
