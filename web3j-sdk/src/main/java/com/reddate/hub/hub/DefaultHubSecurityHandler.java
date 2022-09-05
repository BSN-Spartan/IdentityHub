// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.util.Secp256Util;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;

public class DefaultHubSecurityHandler implements HubSecurityHandler {

  private String privateKey;

  private String publicKey;

  private String uid;

  private CryptoType cryptoType;

  private String extraMsg;

  public DefaultHubSecurityHandler(CryptoType cryptoType, String uid, String privateKey) {
    this.privateKey = privateKey;
    this.uid = uid;
    this.cryptoType = cryptoType;
    this.publicKey = Secp256Util.getPublicKey(cryptoType, privateKey);
  }

  public DefaultHubSecurityHandler(
      CryptoType cryptoType, String uid, String privateKey, String publickey) {
    this.privateKey = privateKey;
    this.uid = uid;
    this.cryptoType = cryptoType;
    this.publicKey = publickey;
  }

  @Override
  public String encrypt(String content) {
    String pubKey = getPublicKey();
    try {
      return Secp256Util.encrypt(cryptoType, content, pubKey);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.ENCRYPT_FAILED.getCode(),
          ErrorMessage.ENCRYPT_FAILED.getMessage() + e.getMessage());
    }
  }

  @Override
  public String decrypt(String content) {
    try {
      return Secp256Util.decrypt(cryptoType, content, privateKey);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.DECRYPT_FAILED.getCode(),
          ErrorMessage.DECRYPT_FAILED.getMessage() + e.getMessage());
    }
  }

  @Override
  public String getPublicKey() {
    return publicKey;
  }

  @Override
  public String getUid() {
    return uid;
  }

  @Override
  public String sign(String content) {
    try {
      return Secp256Util.sign(cryptoType, content, privateKey);
    } catch (Exception e) {
      throw new IdentityHubException(
          ErrorMessage.SIGNATURE_FAILED.getCode(),
          ErrorMessage.SIGNATURE_FAILED.getMessage() + e.getMessage());
    }
  }

  @Override
  public void setExtraMsg(String msg, String sign) {
    extraMsg = HubSecurityHandler.pasrseExtraMsg(msg, sign);
  }

  @Override
  public String getExtraMsg() {
    return extraMsg;
  }

  @Override
  public CryptoType getCryptoType() {
    return cryptoType;
  }
}
