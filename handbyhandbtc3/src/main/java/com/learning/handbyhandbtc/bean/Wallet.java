package com.learning.handbyhandbtc.bean;

import com.learning.handbyhandbtc.utils.RSAUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    private String name;

    public Wallet(String name) {
        this.name = name;

        File priFile = new File(name + ".pri");
        File pubFile = new File(name + ".pub");

        if (!priFile.exists() || priFile.length() == 0 || pubFile.exists() || pubFile.length() == 0) {
            //RSAUtils.generateKeys("RSA", name + ".pri", name + ".pub");
            RSAUtils.generateKeysJS("RSA", name + ".pri", name + ".pub");
        }

        //privateKey = RSAUtils.getPrivateKeyFile("RSA", name + ".pri");
        //publicKey = RSAUtils.getPublicKeyFile("RSA", name + ".pub");
    }

    //转账
    public Transaction sendMoney(String receiverPublicKey, String content) {
        String signature = RSAUtils.getSignature("SHA256withRSA", privateKey, content);

        String senderPublicKey = Base64.encode(publicKey.getEncoded());
        Transaction transaction = new Transaction(senderPublicKey, receiverPublicKey, content, signature);
        return transaction;
    }

    //测试
    public static void main(String[] args) {
        Wallet a = new Wallet("a");
        Wallet b = new Wallet("b");

//        PublicKey publicKey = b.publicKey;
//        String encode = Base64.encode(publicKey.getEncoded());
//
//        Transaction transaction = a.sendMoney(encode, "100");
//
//        boolean verify = transaction.verify();
//        System.out.println(verify);
    }
}
