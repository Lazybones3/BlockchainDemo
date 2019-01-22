package com.learning.handbyhandbtc.bean;

import com.learning.handbyhandbtc.utils.RSAUtils;

import java.security.PublicKey;

public class Transaction {
    //付款方公钥
    public String senderPublicKey;
    //收款方公钥
    public String receiverPublicKey;
    //金额
    public String content;
    //签名
    public String signatureData;

    public Transaction() {
    }

    public Transaction(String senderPublicKey, String receiverPublicKey, String content, String signatureData) {
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
        this.content = content;
        this.signatureData = signatureData;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(String receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }

    //校验交易是否合法
    public boolean verify() {
        PublicKey publicKey = RSAUtils.getPublicKeyFromString("RSA", senderPublicKey);
        return RSAUtils.verify("SHA256withRSA", publicKey, content, signatureData);
//        return RSAUtils.verifyDataJS("SHA256withRSA", publicKey, content, signatureData);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderPublicKey='" + senderPublicKey + '\'' +
                ", receiverPublicKey='" + receiverPublicKey + '\'' +
                ", content='" + content + '\'' +
                ", signatureData='" + signatureData + '\'' +
                '}';
    }
}
