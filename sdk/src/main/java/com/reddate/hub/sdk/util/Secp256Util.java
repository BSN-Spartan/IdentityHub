// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.sdk.util;

import cn.hutool.core.codec.Base64;
import com.reddate.hub.sdk.constant.ErrorMessage;
import com.reddate.hub.sdk.exception.HubException;
import com.reddate.hub.sdk.param.CryptoType;
import com.reddate.hub.sdk.param.KeyPair;
import com.reddate.hub.sdk.util.ECC.Decrypt;
import com.reddate.hub.sdk.util.ECC.Encrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.SignatureException;

/**
 * Secp256 algorithm Utils class,
 * 
 * all Secp256 algorithm relate tolls method implement in this class
 */
public class Secp256Util {

	public static final String TYPE = "Secp256k1";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Generate a key pair
	 * 
	 * @param cryptoType
	 * @return
	 * @throws Exception
	 */
	public static KeyPair createKeyPair(CryptoType cryptoType) {

		KeyPair keyPair = new KeyPair();
		ECKeyPair ecKeyPair = null;
		try {
			ecKeyPair = Keys.createEcKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		keyPair.setType(CryptoType.ECDSA);
		keyPair.setPrivateKey(ecKeyPair.getPrivateKey().toString());
		keyPair.setPublicKey(ecKeyPair.getPublicKey().toString());
		return keyPair;
	}

	/**
	 * Get the public key form private key
	 * 
	 * @param cryptoType
	 * @param privateKey
	 * @return
	 */
	public static String getPublicKey(CryptoType cryptoType, String privateKey) {

		String ecKeyPair = null;
		try {
			ECKeyPair keyPair = ECKeyPair.create(new BigInteger(privateKey));
			ecKeyPair = String.valueOf(keyPair.getPublicKey());
		} catch (Exception e) {
			throw new RuntimeException("invalid privateKey");
		}
		return ecKeyPair;
	}

	public static void main(String[] args) {
		System.out.println(getPublicKey(CryptoType.ECDSA, "177511339483322497155015102112935098674666504693652987703930042787508877099585"));
	}

	/**
	 * Get the address form private key
	 * 
	 * @param cryptoType
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String getAddress(CryptoType cryptoType, String privateKey) {

		String pubKey = getPublicKey(cryptoType, privateKey);
		return Keys.getAddress(pubKey);
	}

	/**
	 * Get the address form public key
	 *
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String getAddress(String publicKey) {

		return Keys.getAddress(publicKey);
	}

	/**
	 * check a private key is a format correct private key or not
	 * 
	 * @param cryptoType
	 * @param privateKey
	 * @return
	 */
	public static Boolean isValidedPrivateKey(CryptoType cryptoType, String privateKey) {
		return (StringUtils.isNotEmpty(privateKey) && NumberUtils.isDigits(privateKey)
				&& new BigInteger(privateKey).compareTo(BigInteger.ZERO) > 0);
	}

	/**
	 * check a public key is a format correct public key or not
	 * 
	 * @param cryptoType
	 * @param publicKey
	 * @return
	 */
	public static Boolean isValidedPublickKey(CryptoType cryptoType, String publicKey) {
		return (StringUtils.isNotEmpty(publicKey) && StringUtils.isAlphanumeric(publicKey)
				&& NumberUtils.isDigits(publicKey));
	}

	/**
	 * Encrypt String data by public key
	 * 
	 * @param cryptoType
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String encrypt(CryptoType cryptoType, String data, String publicKey) {

		try {

			Encrypt encrypts = new Encrypt(new BigInteger(publicKey));
			byte[] bytes = encrypts.encrypt(data.getBytes());
			return Base64.encode(bytes);

		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.ENCRYPT_FAILED.getCode(), ErrorMessage.ENCRYPT_FAILED.getMessage());
		}
	}

	/**
	 * Decrypt String data by private key
	 * 
	 * @param cryptoType
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public static String decrypt(CryptoType cryptoType, String data, String privateKey) {

		try {

			Decrypt decrypt = new Decrypt(new BigInteger(privateKey));
			byte[] bytes = decrypt.decrypt(Base64.decode(data));
			return new String(bytes);

		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.DECRYPT_FAILED.getCode(), ErrorMessage.DECRYPT_FAILED.getMessage());
		}
	}

	/**
	 * Get the sign for String data and private key
	 * 
	 * @param cryptoType
	 * @param message
	 * @param privateKey
	 * @return return the sign value
	 */
	public static String sign(CryptoType cryptoType, String message, String privateKey) {

		try {
			String publicKey = getPublicKey(cryptoType, privateKey);
			Sign.SignatureData signatureData = sign(message.getBytes(), new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey)));
			return secp256k1SigBase64Serialization(signatureData);

		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.SIGNATURE_FAILED.getCode(), ErrorMessage.SIGNATURE_FAILED.getMessage());
		}
	}

	private static String secp256k1SigBase64Serialization(Sign.SignatureData sigData) {

		byte[] sigBytes = new byte[65];
		sigBytes[64] = sigData.getV()[0];
		System.arraycopy(sigData.getR(), 0, sigBytes, 0, 32);
		System.arraycopy(sigData.getS(), 0, sigBytes, 32, 32);
		return new String(base64Encode(sigBytes), StandardCharsets.UTF_8);
	}

	private static byte[] base64Encode(byte[] nonBase64Bytes) {
		return org.bouncycastle.util.encoders.Base64.encode(nonBase64Bytes);
	}

	private static Sign.SignatureData sign(byte[] message, ECKeyPair keyPair) {
		return Sign.signMessage(message, keyPair, true);
	}

	/**
	 * Vefiry the sign by String message and public and sign value
	 * 
	 * @param cryptoType
	 * @param message
	 * @param publicKey
	 * @param signValue
	 * @return
	 * @throws SignatureException
	 */
	public static boolean verify(CryptoType cryptoType, String message, String publicKey, String signValue) {

		try {

			byte[] bytes = Hash.sha3(message.getBytes(StandardCharsets.UTF_8));
			BigInteger bigInteger = new BigInteger(publicKey);
			Sign.SignatureData signatureData = secp256k1SigBase64Deserialization(signValue);
			return verify(bytes, bigInteger, signatureData);

		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.VALIDATE_SIGN_ERROR.getCode(),
					ErrorMessage.VALIDATE_SIGN_ERROR.getMessage());
		}
	}

	private static Sign.SignatureData secp256k1SigBase64Deserialization(String signature) {

		byte[] sigBytes = base64Decode(signature.getBytes(StandardCharsets.UTF_8));
		byte[] r = new byte[32];
		byte[] s = new byte[32];
		System.arraycopy(sigBytes, 0, r, 0, 32);
		System.arraycopy(sigBytes, 32, s, 0, 32);
		return new Sign.SignatureData(sigBytes[64], r, s);
	}

	private static byte[] base64Decode(byte[] base64Bytes) {
		return org.bouncycastle.util.encoders.Base64.decode(base64Bytes);
	}

	private static boolean verify(byte[] hash, BigInteger publicKey, Sign.SignatureData signatureData) {

		ECDSASignature sig = new ECDSASignature(Numeric.toBigInt(signatureData.getR()), Numeric.toBigInt(signatureData.getS()));
		byte[] v = signatureData.getV();
		byte recld = v[0];
		BigInteger k = Sign.recoverFromSignature(recld - 27, sig, hash);
		return publicKey.equals(k);
	}


}
