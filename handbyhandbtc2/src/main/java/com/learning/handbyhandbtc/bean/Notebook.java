package com.learning.handbyhandbtc.bean;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.handbyhandbtc.utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Notebook {

    //保存数据的集合
    private ArrayList<Block> list = new ArrayList<>();

    public Notebook() {
        init();
    }

    private void init() {
        File file = new File("a.json");
        try {
            if (file.exists() && file.length() > 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                //反序列化
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Block.class);
                list = objectMapper.readValue(file, javaType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //添加封面，创世区块
    public void addGenesis(String genesis) {
        if (list.size() > 0) {
            //添加时必须是新账本
            throw new RuntimeException("添加封面时，必须是新账本");
        }

        //创世区块没有上一个hash值，设置一个64位的数字作为初始值
        String preHash = "123456781234567812345678123456781234567812345678123456781234567812345678";
        //模拟挖矿,得到工作量证明
        int nonce = mine(genesis + preHash);

        list.add(new Block(
                list.size() + 1,    //id
                genesis,                //内容
                HashUtils.sha256(nonce + genesis + preHash),   //hash值
                nonce,                   //工作量证明
                preHash                 //上一个hash值
        ));
        save2Disk();
    }

    private int mine(String content) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String hash = HashUtils.sha256(i + content);
            if (hash.startsWith("0000")) {
                System.out.println("挖矿成功:" + i);
                return i;
            } else {
                System.out.println("这是第" + i + "次尝试挖矿");
            }
        }
        throw new RuntimeException("挖矿失败！");
    }

    //添加交易记录，普通区块
    public void addNote(String note) {
        if(list.size() < 1) {
            //添加时必须已经有封面
            throw new RuntimeException("添加记录时，必须已经有封面");
        }

        Block block = list.get(list.size() - 1);
        String preHash = block.hash;
        int nonce = mine(note + preHash);

        list.add(new Block(
                list.size() + 1,    //id
                note,                //内容
                HashUtils.sha256(nonce + note + preHash),   //hash值
                nonce,                   //工作量证明
                preHash                 //上一个hash值
        ));
        save2Disk();
    }

    //展示交易记录
    public ArrayList<Block> showlist() {
        return list;
    }

    //保存数据 xml json
    public void save2Disk() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //序列化
            objectMapper.writeValue(new File("a.json"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String check() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            Block block = list.get(i);
            String content = block.content;
            String hash = block.hash;
            int nonce = block.nonce;
            String preHash = block.preHash;

            if (i == 0) {
                //创世区块只要校验hash
                String caculateHash = HashUtils.sha256(nonce + content + preHash);
                if (!caculateHash.equals(hash)) {
                    sb.append("编号为"+ block.id +"的区块的hash有问题<br>");
                }
            } else {
                //普通区块要校验hash和preHash
                String caculateHash = HashUtils.sha256(nonce + content + preHash);
                if (!caculateHash.equals(hash)) {
                    sb.append("编号为"+ block.id +"的区块的hash有问题<br>");
                }

                Block preBlock = list.get(i - 1);
                String preBlockHash = preBlock.hash;
                if (!preBlockHash.equals(preHash)) {
                    sb.append("编号为"+ block.id +"的区块的preHash有问题<br>");
                }
            }
        }
        return sb.toString();
    }
}
