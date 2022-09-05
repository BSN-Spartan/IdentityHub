// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.util.commons;

import cn.hutool.core.codec.Base64;
import com.reddate.hub.protocol.common.KeyPair;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class ECDSAUtils {

  public static final String type = "Secp256k1";
  public static final String signFlag = "SHA256withECDSA";
  private static String data = "ecdsa security";

  /**
   * Generate public and private keys
   *
   * @author shaopengfei
   */
  public static KeyPair createKey() throws Exception {
    KeyPair keyPair = new KeyPair();
    ECKeyPair keyPairOriginal = Keys.createEcKeyPair();
    keyPair.setPublicKey(keyPairOriginal.getPublicKey().toString());
    keyPair.setPrivateKey(keyPairOriginal.getPrivateKey().toString());
    keyPair.setType(ECDSAUtils.type);
    return keyPair;
  }

  /**
   * The message generates a signature based on the private key
   *
   * @param message
   * @param privateKey
   * @param publicKey
   * @author shaopengfei
   */
  public static String sign(String message, String privateKey, String publicKey) throws Exception {

    try {
      Sign.SignatureData signatureData = sign(
                      message.getBytes(),
                      new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey)));
      return secp256k1SigBase64Serialization(signatureData);

    } catch (Exception e) {
      // e.printStackTrace();
      throw new Exception("invalid privateKey or publicKey");
    }
  }

  private static Sign.SignatureData sign(byte[] message, ECKeyPair keyPair) {
    return Sign.signMessage(message, keyPair, true);
  }

  private static String secp256k1SigBase64Serialization(Sign.SignatureData sigData) {
    byte[] sigBytes = new byte[65];
    sigBytes[64] = sigData.getV()[0];
    System.arraycopy(sigData.getR(), 0, sigBytes, 0, 32);
    System.arraycopy(sigData.getS(), 0, sigBytes, 32, 32);
    return new String(Base64.encode(sigBytes));
  }

  /**
   * Sign the message according to the private key
   *
   * @param message
   * @param privateKey
   * @author shaopengfei
   */
  public static String sign(String message, String privateKey) throws Exception {
    /*SignatureData sigData = secp256k1SignToSignature(message, new BigInteger(privateKey));
    return secp256k1SigBase64Serialization(sigData);*/
    return null;
  }

  /*public static SignatureData secp256k1SignToSignature(String rawData, BigInteger privateKey) {
    ECKeyPair keyPair = GenCredential.createKeyPair(privateKey.toString(16));
    return secp256k1SignToSignature(rawData, keyPair);
  }

  public static SignatureData secp256k1SignToSignature(String rawData, ECKeyPair keyPair) {
    ECDSASign ecdsaSign = new ECDSASign();
    return ecdsaSign.secp256SignMessage(rawData.getBytes(StandardCharsets.UTF_8), keyPair);
  }

  public static String secp256k1SigBase64Serialization(SignatureData sigData) {
    byte[] sigBytes = new byte[65];
    sigBytes[64] = sigData.getV();
    System.arraycopy(sigData.getR(), 0, sigBytes, 0, 32);
    System.arraycopy(sigData.getS(), 0, sigBytes, 32, 32);
    return new String(base64Encode(sigBytes), StandardCharsets.UTF_8);
  }*/

  /**
   * Verify the signature of the message according to the public key
   *
   * @param message
   * @param publicKey
   * @param signValue
   * @author shaopengfei
   */
  public static boolean verify(String message, String publicKey, String signValue) throws Exception {

    try {
      byte[] bytes = Hash.sha3(message.getBytes());
      BigInteger bigInteger = new BigInteger(publicKey);
      Sign.SignatureData signatureData = secp256k1SigBase64Deserialization(signValue);
      return verify(bytes, bigInteger, signatureData);
    } catch (Exception e) {
       e.printStackTrace();
      throw new Exception("invalid publicKey");
    }
  }

  private static boolean verify( byte[] hash, BigInteger publicKey, Sign.SignatureData signatureData) {

    ECDSASignature sig = new ECDSASignature( Numeric.toBigInt(signatureData.getR()), Numeric.toBigInt(signatureData.getS()));
    byte[] v = signatureData.getV();
    byte recld = v[0];
    BigInteger k = Sign.recoverFromSignature(recld - 27, sig, hash);
    return publicKey.equals(k);
  }

  private static Sign.SignatureData secp256k1SigBase64Deserialization(String signature) {

    byte[] sigBytes = Base64.decode(signature);
    byte[] r = new byte[32];
    byte[] s = new byte[32];
    System.arraycopy(sigBytes, 0, r, 0, 32);
    System.arraycopy(sigBytes, 32, s, 0, 32);
    return new Sign.SignatureData(sigBytes[64], r, s);
  }

  /*public static SignatureData secp256k1SigBase64Deserialization(String signature) {
    byte[] sigBytes = base64Decode(signature.getBytes(StandardCharsets.UTF_8));
    byte[] r = new byte[32];
    byte[] s = new byte[32];
    System.arraycopy(sigBytes, 0, r, 0, 32);
    System.arraycopy(sigBytes, 32, s, 0, 32);
    return new SignatureData(sigBytes[64], r, s);
  }*/

  private static String secp256k1Sign(String rawData, BigInteger privateKey) {
    /*Sign.SignatureData sigData = secp256k1SignToSignature(rawData, privateKey);
    return secp256k1SigBase64Serialization(sigData);*/
    return null;
  }

  private static boolean verifySecp256k1Signature(
      String rawData, String signatureBase64, BigInteger publicKey) {
    if (rawData == null) {
      return false;
    }
    /*SM3Digest sm3Digest = new SM3Digest();
    byte[] hashBytes = sm3Digest.hash(rawData.getBytes(StandardCharsets.UTF_8));
    Sign.SignatureData sigData = secp256k1SigBase64Deserialization(signatureBase64, publicKey);
    return SM2Sign.verify(hashBytes, sigData);*/
    return false;
  }

  /*private static Sign.SignatureData secp256k1SigBase64Deserialization(
      String signature, BigInteger publicKey) {
    byte[] sigBytes = base64Decode(signature.getBytes(StandardCharsets.UTF_8));
    byte[] r = new byte[32];
    byte[] s = new byte[32];
    System.arraycopy(sigBytes, 0, r, 0, 32);
    System.arraycopy(sigBytes, 32, s, 0, 32);

    return new Sign.SignatureData(sigBytes[64], r, s, Numeric.toBytesPadded(publicKey, 64));
  }*/

  private static byte[] base64Encode(byte[] nonBase64Bytes) {
    return org.bouncycastle.util.encoders.Base64.encode(nonBase64Bytes);
  }

  private static byte[] base64Decode(byte[] base64Bytes) {
    return org.bouncycastle.util.encoders.Base64.decode(base64Bytes);
  }
}
