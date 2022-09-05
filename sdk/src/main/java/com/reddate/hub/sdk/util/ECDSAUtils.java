package com.reddate.hub.sdk.util;


import com.reddate.hub.sdk.constant.ErrorMessage;
import com.reddate.hub.sdk.exception.HubException;
import com.reddate.hub.sdk.protocol.common.KeyPair;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;


/**
 * ECDSAU utils class,
 * some common tools method implement in this class
 */
public class ECDSAUtils {

    public final static String TYPE = "Secp256k1";
    public final static String SIGN_FLAG = "SHA256withECDSA";
    private static String data = "ecdsa security";

    /**
     * Generate public and private keys
     * 
     * @return return public and private key
     * 
     */
    public static KeyPair createKey() throws Exception{
    	KeyPair keyPair = new KeyPair();
        ECKeyPair keyPairOriginal = Keys.createEcKeyPair();
        keyPair.setPublicKey(keyPairOriginal.getPublicKey().toString());
        keyPair.setPrivateKey(keyPairOriginal.getPrivateKey().toString());
        keyPair.setType(ECDSAUtils.TYPE);
        return keyPair;
    }

    /**
     * get public key from private key
     * 
     * @param privateKey
     * @return
     */
    public static String getPublicKey(String privateKey) {
        try {
            ECKeyPair keyPair = ECKeyPair.create(new BigInteger(privateKey));
            return String.valueOf(keyPair.getPublicKey());
        }catch (Exception e){
            e.printStackTrace();
            throw new HubException(ErrorMessage.PRIVATE_KEY_ILLEGAL_FORMAT.getCode(),
					ErrorMessage.PRIVATE_KEY_ILLEGAL_FORMAT.getMessage());
        }
    }

    /**
     * Sign the message according to the private key
     * 
     * @param message
     * @param privateKey
     */
    public static String sign(String message, String privateKey){

        String publicKey = getPublicKey(privateKey);
    	try {
            Sign.SignatureData signatureData = sign(message.getBytes(), new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey)));
            return secp256k1SigBase64Serialization(signatureData);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HubException(ErrorMessage.SIGNATURE_FAILED.getCode(), ErrorMessage.SIGNATURE_FAILED.getMessage());
		}
    }

    private static Sign.SignatureData sign(byte[] message, ECKeyPair keyPair) {
        return Sign.signMessage(message, keyPair, true);
    }

    public static void main(String[] args) {

        String sign = sign("hub:bsn:BNs5BgsEU7PjWdAKE8ZLifsZ2cE", "16780098087725241378027720583043134163962766226428001721995781676508526587610");
        System.out.println("------------------- 1 ---- sign = " + sign);
    }

    private static String secp256k1SigBase64Serialization(Sign.SignatureData sigData) {

        byte[] sigBytes = new byte[65];
        sigBytes[64] = sigData.getV()[0];
        System.arraycopy(sigData.getR(), 0, sigBytes, 0, 32);
        System.arraycopy(sigData.getS(), 0, sigBytes, 32, 32);
        return new String(base64Encode(sigBytes), StandardCharsets.UTF_8);
    }

    /**
     * Verifying the signature of a message based on the public key
     *
     * @param message
     * @param publicKey
     * @param signValue
     * @author shaopengfei
     */
    public static boolean verify(String message, String publicKey, String signValue) throws Exception {

        byte[] bytes = Hash.sha3(message.getBytes(StandardCharsets.UTF_8));
        BigInteger bigInteger = new BigInteger(publicKey);
        Sign.SignatureData signatureData = secp256k1SigBase64Deserialization(signValue);
        return verify(bytes, bigInteger, signatureData);
    }

    private static boolean verify(byte[] hash, BigInteger publicKey, Sign.SignatureData signatureData) {

        ECDSASignature sig = new ECDSASignature(Numeric.toBigInt(signatureData.getR()), Numeric.toBigInt(signatureData.getS()));
        byte[] v = signatureData.getV();
        byte recld = v[0];
        BigInteger k = Sign.recoverFromSignature(recld - 27, sig, hash);
        return publicKey.equals(k);
    }

    private static Sign.SignatureData secp256k1SigBase64Deserialization(String signature) {

        byte[] sigBytes = base64Decode(signature.getBytes(StandardCharsets.UTF_8));
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(sigBytes, 0, r, 0, 32);
        System.arraycopy(sigBytes, 32, s, 0, 32);
        return new Sign.SignatureData(sigBytes[64], r, s);
    }

    private static byte[] base64Encode(byte[] nonBase64Bytes) {
        return org.bouncycastle.util.encoders.Base64.encode(nonBase64Bytes);
    }

    private static byte[] base64Decode(byte[] base64Bytes) {
        return org.bouncycastle.util.encoders.Base64.decode(base64Bytes);
    }

}
