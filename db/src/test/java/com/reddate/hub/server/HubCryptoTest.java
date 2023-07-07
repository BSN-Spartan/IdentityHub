package com.reddate.hub.server;

import com.reddate.hub.constant.CryptoType;
import com.reddate.hub.pojo.KeyPair;
import com.reddate.hub.server.config.HubApplicationStartingEventListener;
import com.reddate.hub.util.AesUtils;
import com.reddate.hub.util.Secp256Util;
import org.junit.jupiter.api.Test;


public class HubCryptoTest {

    @Test
    public void test() throws Exception {
        KeyPair keyPair = Secp256Util.createKeyPair(CryptoType.ECDSA);
        String hubPrivateKey = keyPair.getPrivateKey();
        String aesPwd = AesUtils.generalKey(HubApplicationStartingEventListener.DEFUALT_PWD);
        String encptyHubPrivateKey = AesUtils.encrypt(hubPrivateKey, aesPwd);;
        System.out.println("HubPrivateKey="+encptyHubPrivateKey);
        System.out.println("HubPublicKey="+keyPair.getPublicKey());
    }
}
