package com.learning.handbyhandbtc.websocket;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.handbyhandbtc.bean.Block;
import com.learning.handbyhandbtc.bean.MessageBean;
import com.learning.handbyhandbtc.bean.Notebook;
import com.learning.handbyhandbtc.bean.Transaction;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

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
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MessageBean messageBean = objectMapper.readValue(message, MessageBean.class);

            Notebook notebook = Notebook.getInstance();
            if (messageBean.type == 1) {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Block.class);
                ArrayList<Block> newList = objectMapper.readValue(messageBean.msg, javaType);

                notebook.compareData(newList);
            } else if (messageBean.type == 2) {
                Transaction transaction = objectMapper.readValue(messageBean.msg, Transaction.class);
                if (transaction.verify()) {
                    notebook.addNote(messageBean.msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
