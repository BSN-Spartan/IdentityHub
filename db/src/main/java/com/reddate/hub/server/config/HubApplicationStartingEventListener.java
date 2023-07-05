// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.hub.constant.ErrorMessage;
import com.reddate.hub.hub.exception.IdentityHubException;
import com.reddate.hub.pojo.KeyPair;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.util.Secp256Util;

import javax.annotation.Resource;

public class HubApplicationStartingEventListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

  private static final Logger logger =
          LoggerFactory.getLogger(HubApplicationStartingEventListener.class);

  public static final String SPRING_CONFIG_LOCATION = "spring.config.location";

  public static final String PWD = "pwd";

  public static final String DEFUALT_PWD = "bdfhiklmnoqrsuvxyzBCEGIMORSUVW";

  public static final String HUB_PRIVATE_KEY = "hub.privateKey";

  public static final String HUB_PUBLIC_KEY = "hub.publicKey";

  public static final String HUB_CRYPTO_TYPE = "hub.cryptoType";

  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    String cfgFile = getConfigFilePath(event.getArgs());
    String pwd = getPwd(event.getArgs());
    createHubKey(cfgFile, pwd);
  }

  private String getConfigFilePath(String[] args) {
    String location = null;
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        String arg = args[i];
        if (arg != null) {
          arg = arg.toLowerCase().trim();
          if (arg.contains(SPRING_CONFIG_LOCATION)) {
            String[] tmp = arg.split("=");
            if (tmp.length != 2) {
              throw new RuntimeException("argument 'spring.config.location' is in correct format");
            }
            if (tmp[1] == null || tmp[1].trim().isEmpty()) {
              throw new RuntimeException("argument 'spring.config.location' is in correct format");
            }
            location = tmp[1].trim();
            location = location.replace("\\", "/");
            break;
          }
        }
      }
    }

    if (location == null || location.trim().isEmpty()) {
//      String descDir = HubApplicationStartingEventListener.class.getResource("").toString();
//      descDir =
//          descDir.substring(6, descDir.length() - "com/reddate/hub/server/config/".length())
//              + "application.properties";
//      location = descDir;
      location =  "application.properties";
    }

    return location;
  }

  private String getPwd(String[] args) {
    String pwd = null;
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        String arg = args[i];
        if (arg != null) {
          arg = arg.toLowerCase().trim();
          if (arg.contains(PWD)) {
            String[] tmp = arg.split("=");
            if (tmp.length != 2) {
              throw new RuntimeException("argument 'pwd' is in correct format");
            }
            if (tmp[1] == null || tmp[1].trim().isEmpty()) {
              throw new RuntimeException("argument 'pwd' is in correct format");
            }
            pwd = tmp[1].trim();
            break;
          }
        }
      }
    }

    if (pwd == null || pwd.trim().isEmpty()) {
      pwd = DEFUALT_PWD;
    }

    return pwd;
  }

  private void createHubKey(String cfgFile, String pwd) {
    logger.info("the Identify Hub config file path is : " + cfgFile);
    InputStream inputStream = null;
    try {
      inputStream = this.getClass().getClassLoader().getResourceAsStream(cfgFile);
      //inputStream = new FileInputStream(cfgFile);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("The config file [" + cfgFile + "] do not exists");
    }

    Properties prop = new Properties();
    try {
      prop.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("The config file [" + cfgFile + "]  format is incorrect");
    }

    CryptoType cryptoType = null;
    String hubCryptoType = null;
    try {
      hubCryptoType = prop.getProperty(HUB_CRYPTO_TYPE);
      cryptoType = CryptoType.ofVlaue(Integer.parseInt(hubCryptoType.trim()));
    } catch (Exception e1) {
      if (hubCryptoType == null || hubCryptoType.trim().isEmpty()) {
        throw new RuntimeException("crypto type is empty");
      } else {
        throw new RuntimeException("crypto type is incorrect");
      }
    }

    String hubPrivateKey = prop.getProperty(HUB_PRIVATE_KEY);
    String hubPublicKey = prop.getProperty(HUB_PUBLIC_KEY);
    if (hubPrivateKey == null
            || hubPrivateKey.trim().isEmpty()
            || hubPublicKey == null
            || hubPublicKey.trim().isEmpty()) {
      String encptyHubPrivateKey = null;
      try {
        KeyPair keyPair = Secp256Util.createKeyPair(cryptoType);
        hubPrivateKey = keyPair.getPrivateKey();
        String aesPwd = AesUtils.generalKey(pwd);
        encptyHubPrivateKey = AesUtils.encrypt(hubPrivateKey, aesPwd);
        prop.setProperty(HUB_PRIVATE_KEY, encptyHubPrivateKey);
        hubPublicKey = keyPair.getPublicKey();
        prop.setProperty(HUB_PUBLIC_KEY, hubPublicKey);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Generatem hub private key and public key failed");
      }

      OutputStream outputStream = null;
      try {
        outputStream = new FileOutputStream(cfgFile);
        prop.store(outputStream, null);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        throw new RuntimeException("hub private key and public key to file failed");
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("hub private key and public key to file failed");
      } finally {
        if (outputStream != null) {
          try {
            outputStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      HubConfigUtils.refreshHubConfig(cfgFile, hubPrivateKey, hubPublicKey, cryptoType);
      logger.info(
              "===============================================================================================");
      logger.info("     ");
      logger.info(
              "==========         " + "The Encryption Hub Private Key is : " + encptyHubPrivateKey);
      logger.info("==========         " + "The Hub Public Key is : " + hubPublicKey);
      logger.info("     ");
      logger.info(
              "===============================================================================================");
    } else {
      try {
        String aesPwd = AesUtils.generalKey(pwd);
        hubPrivateKey = AesUtils.decrypt(hubPrivateKey, aesPwd);
      } catch (Exception e) {
        e.printStackTrace();
        throw new IdentityHubException(
                ErrorMessage.DECRYPT_HUB_PK_FAILED.getCode(),
                ErrorMessage.DECRYPT_HUB_PK_FAILED.getMessage());
      }
      HubConfigUtils.refreshHubConfig(cfgFile, hubPrivateKey, hubPublicKey, cryptoType);
    }
  }
}
