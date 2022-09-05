// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.utils.ECC;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECPrivateKeySpec;

public class Decrypt {
  private final BigInteger priKey;
  private final BCECPrivateKey bCECPrivateKey;

  public Decrypt(BigInteger privateKey) {
    this.priKey = privateKey;
    this.bCECPrivateKey = createBCECPrivateKey(privateKey);
  }

  public BigInteger getPriKey() {
    return priKey;
  }

  public BCECPrivateKey getBCECPrivateKey() {
    return bCECPrivateKey;
  }

  /**
   * create BCECPrivateKey from privateKey
   *
   * @param privateKey
   * @return
   */
  private BCECPrivateKey createBCECPrivateKey(BigInteger privateKey) {
    // Handle secret key
    ECPrivateKeySpec secretKeySpec = new ECPrivateKeySpec(privateKey, Params.ecNamedCurveSpec);
    BCECPrivateKey bcecPrivateKey =
        new BCECPrivateKey("ECDSA", secretKeySpec, BouncyCastleProvider.CONFIGURATION);
    return bcecPrivateKey;
  }

  /**
   * Decrypt the data which encryt by ECC
   *
   * @param data
   * @return
   * @throws NoSuchPaddingException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchProviderException
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public byte[] decrypt(byte[] data)
      throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
          InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
          IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance(" ECIES", "BC");
    cipher.init(Cipher.DECRYPT_MODE, getBCECPrivateKey(), Params.IES_PARAMS);

    return cipher.doFinal(data);
  }
}
