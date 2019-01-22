package com.learning.handbyhandbtc.utils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    public static void generateKeysJS(String algorithm, String privateKeyPath, String publicKeyPath) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            byte[] privateKeyEncoded = privateKey.getEncoded();
            byte[] publicKeyEncoded = publicKey.getEncoded();
            String encodePrivateKey = Base64.encode(privateKeyEncoded);
            String encodePublicKey = Base64.encode(publicKeyEncoded);
            FileUtils.writeStringToFile(new File(privateKeyPath), "-----BEGIN PRIVATE KEY-----\n" + encodePrivateKey + "\n-----END PRIVATE KEY-----", "UTF-8");
            FileUtils.writeStringToFile(new File(publicKeyPath), encodePublicKey, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey getPrivateKeyFile(String algorithm, String privateKeyPath) {
        try {
            String privateKeyString = FileUtils.readFileToString(new File(privateKeyPath), Charset.defaultCharset());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyString));
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey getPublicKeyFile(String algorithm, String publicKeyPath) {
        try {
            String publicKeyString = FileUtils.readFileToString(new File(publicKeyPath), Charset.defaultCharset());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(publicKeyString));
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey getPublicKeyFromString(String algorithm, String publicKeyString) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(publicKeyString));
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSignature(String signatureAlgorithm, PrivateKey privateKey, String input) {
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initSign(privateKey);
            signature.update(input.getBytes());
            byte[] sign = signature.sign();
            return Base64.encode(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verify(String signatureAlgorithm, PublicKey publicKey, String input, String signatured) {
        try {
            //获取签名对象
            Signature signature = Signature.getInstance(signatureAlgorithm);
            //传入公钥
            signature.initVerify(publicKey);
            //传入原文
            signature.update(input.getBytes());
            //校验签名
            return signature.verify(Base64.decode(signatured));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyDataJS(String signatureAlgorithm, PublicKey publicKey, String input, String signatured) {
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initVerify(publicKey);
            signature.update(input.getBytes());
            return signature.verify(toBytes(signatured));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //前台的签名结果是将byte中的一些负数转换成了正数，
    //但是后台验证的方法需要的又必须是转换之前的
    private static byte[] toBytes(String signatured) {
        int k = 0;
        byte[] results = new byte[signatured.length() / 2];
        for (int i = 0; i + 1 < signatured.length(); i += 2, k++) {
            results[k] = (byte) (Character.digit(signatured.charAt(i), 16) << 4);
            results[k] += (byte) (Character.digit(signatured.charAt(i + 1), 16));
        }
        return results;
    }


}
