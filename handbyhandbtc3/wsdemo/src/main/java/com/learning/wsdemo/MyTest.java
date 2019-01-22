package com.learning.wsdemo;

import java.net.URI;

public class MyTest {
    public static void main(String[] args) throws Exception {
        MyServer server = new MyServer(7000);
        server.startServer();

        URI uri = new URI("ws://localhost:7000");
        MyClient client1 = new MyClient(uri, "No1");
        MyClient client2 = new MyClient(uri, "No2");

        client1.connect();
        client2.connect();

        Thread.sleep(1000);
//        server.broadcast("只是服务器发送的消息");

        client1.send("这是客户端1发送的消息");
    }
}
