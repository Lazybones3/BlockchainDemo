package com.learning.handbyhandbtc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.handbyhandbtc.HandbyhandbtcApplication;
import com.learning.handbyhandbtc.bean.Block;
import com.learning.handbyhandbtc.bean.MessageBean;
import com.learning.handbyhandbtc.bean.Notebook;
import com.learning.handbyhandbtc.bean.Transaction;
import com.learning.handbyhandbtc.websocket.MyClient;
import com.learning.handbyhandbtc.websocket.MyServer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
public class BlockController {

    private Notebook notebook = Notebook.getInstance();


    @RequestMapping(value = "/addGenesis", method = RequestMethod.POST)
    public String addGenesis(String genesis) {
        try {
            notebook.addGenesis(genesis);
            return "添加封面成功";
        } catch (Exception e) {
            return "添加封面失败" + e.getMessage();
        }
    }

    @RequestMapping(value = "/addNote", method = RequestMethod.POST)
    public String addNote(Transaction transaction) {
        try {
            if (transaction.verify()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String transactionString = objectMapper.writeValueAsString(transaction);
                //挖到矿以后广播数据通知其他节点
                MessageBean messageBean = new MessageBean(2, transactionString);
                String msg = objectMapper.writeValueAsString(messageBean);
                server.broadcast(msg);

                notebook.addNote(transactionString);
                return "添加记录成功";
            } else {
                throw new RuntimeException("交易数据校验失败");
            }

        } catch (Exception e) {
            return "添加记录失败" + e.getMessage();
        }
    }

    @RequestMapping(value = "/showlist", method = RequestMethod.GET)
    public ArrayList<Block> showlist() {
        return notebook.showlist();
    }

    //校验数据
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check() {
        String check = notebook.check();
        if (StringUtils.isEmpty(check)) {
            return "数据安全";
        }
        return check;
    }

    private MyServer server;

    //在构造函数调用的时候调用init
    @PostConstruct
    public void init() {
        server = new MyServer(Integer.parseInt(HandbyhandbtcApplication.port) + 1);
        server.startServer();
    }

    private HashSet<String> set = new HashSet<>();

    //节点注册
    @RequestMapping("/register")
    public String register(String node) {
        set.add(node);
        return "添加成功";
    }

    private ArrayList<MyClient> clients = new ArrayList<>();
    //连接
    @RequestMapping("/conn")
    public String conn() {
        try {
            for (String s : set) {
                URI uri = new URI("ws://localhost:" + s);
                MyClient client = new MyClient(uri, s);
                client.connect();
                clients.add(client);
            }
            return "连接成功";
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "连接失败:" + e.getMessage();
        }

    }

    //广播
    @RequestMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }

    //同步数据
    @RequestMapping("/syncData")
    public String syncData() {
        for (MyClient client : clients) {
            client.send("把你的区块链数据给我一份");
        }
        return "广播成功";
    }
}
