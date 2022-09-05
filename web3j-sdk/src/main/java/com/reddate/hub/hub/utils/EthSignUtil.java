// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

public class EthSignUtil {

  private static Sign.SignatureData sign(byte[] message, ECKeyPair keyPair) {
    return Sign.signMessage(message, keyPair, true);
  }

  public static String sign(String message, String privateKey) {
    try {
      Sign.SignatureData signatureData =
          sign(
              message.getBytes(StandardCharsets.UTF_8),
              ECKeyPair.create(new BigInteger(privateKey)));
      return secp256k1SigBase64Serialization(signatureData);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("invalid privateKey");
    }
  }

  public static boolean verify(String message, String publicKey, String signValue) {
    try {
      byte[] bytes = Hash.sha3(message.getBytes(StandardCharsets.UTF_8));
      BigInteger bigInteger = new BigInteger(publicKey);
      Sign.SignatureData signatureData = secp256k1SigBase64Deserialization(signValue);
      return verify(bytes, bigInteger, signatureData);
    } catch (Exception e) {
      throw new RuntimeException("invalid publicKey");
    }
  }

  private static boolean verify(
      byte[] hash, BigInteger publicKey, Sign.SignatureData signatureData) {
    ECDSASignature sig =
        new ECDSASignature(
            Numeric.toBigInt(signatureData.getR()), Numeric.toBigInt(signatureData.getS()));
    byte[] v = signatureData.getV();
    byte recld = v[0];
    BigInteger k = Sign.recoverFromSignature(recld - 27, sig, hash);
    return publicKey.equals(k);
  }

  private static String secp256k1SigBase64Serialization(Sign.SignatureData sigData) {
    byte[] sigBytes = new byte[65];
    sigBytes[64] = sigData.getV()[0];
    System.arraycopy(sigData.getR(), 0, sigBytes, 0, 32);
    System.arraycopy(sigData.getS(), 0, sigBytes, 32, 32);
    return Numeric.toHexString(sigBytes);
  }

  private static Sign.SignatureData secp256k1SigBase64Deserialization(String signature) {
    byte[] sigBytes = Numeric.hexStringToByteArray(signature);
    byte[] r = new byte[32];
    byte[] s = new byte[32];
    System.arraycopy(sigBytes, 0, r, 0, 32);
    System.arraycopy(sigBytes, 32, s, 0, 32);
    return new Sign.SignatureData(sigBytes[64], r, s);
  }
}
