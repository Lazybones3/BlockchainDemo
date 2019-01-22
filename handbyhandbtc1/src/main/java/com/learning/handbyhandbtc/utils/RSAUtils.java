package com.learning.handbyhandbtc.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    public static void generateKeys(String algorithm, String privateKeyPath, String publicKeyPath) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            byte[] privateKeyEncoded = privateKey.getEncoded();
            byte[] publicKeyEncoded = publicKey.getEncoded();
            String encodePrivateKey = Base64.encode(privateKeyEncoded);
            String encodePublicKey = Base64.encode(publicKeyEncoded);
            FileUtils.writeStringToFile(new File(privateKeyPath), encodePrivateKey, Charset.defaultCharset());
            FileUtils.writeStringToFile(new File(publicKeyPath), encodePublicKey, Charset.defaultCharset());
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
            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initVerify(publicKey);
            signature.update(input.getBytes());
            return signature.verify(Base64.decode(signatured));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
