package com.learning.handbyhandbtc.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.handbyhandbtc.bean.Block;
import com.learning.handbyhandbtc.bean.MessageBean;
import com.learning.handbyhandbtc.bean.Notebook;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

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
        try {
            if ("把你的区块链数据给我一份".equals(message)) {
                Notebook notebook = Notebook.getInstance();
                ArrayList<Block> list = notebook.showlist();

                ObjectMapper objectMapper = new ObjectMapper();
                String blockChainData = objectMapper.writeValueAsString(list);

                MessageBean messageBean = new MessageBean(1, blockChainData);
                String msg = objectMapper.writeValueAsString(messageBean);
                //广播
                this.broadcast(msg);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
