package com.learning.handbyhandbtc.bean;

public class Block {

    public int id;
    public String content;
    public String hash;
    public int nonce;
    public String preHash;

    public Block() {
    }

    public Block(int id, String content, String hash, int nonce, String preHash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
        this.nonce = nonce;
        this.preHash = preHash;
    }
}
