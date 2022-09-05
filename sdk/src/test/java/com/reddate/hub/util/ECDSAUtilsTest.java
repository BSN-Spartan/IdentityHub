package com.reddate.hub.util;

import com.reddate.hub.sdk.util.ECDSAUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class ECDSAUtilsTest {
    @Test
    public void testCreateKey() throws Exception {
        assertEquals(ECDSAUtils.TYPE, ECDSAUtils.createKey().getType());
    }

    @Test
    public void testGetPublicKey() {
        assertThrows(RuntimeException.class, () -> ECDSAUtils.getPublicKey("Private Key"));
        assertEquals("1333193071510143823680032068730801233544396975037522064189058407646457415432098030100736581188409203"
                + "9760895910548088435115588884684157831830796456056960436", ECDSAUtils.getPublicKey("42"));
    }

    @Test
    public void testSign() throws Exception {
        assertEquals("9L7S9k/RUFqYjjfNiIdKpdqgNOjlE1eNlergfWSAAkwTBzoeQwGHn+P4Rr13C9L2dRQUQHGmMfEDbXKtAyhplQA=",
                ECDSAUtils.sign("Not all who wander are lost", "42"));
        assertEquals("rRGRFzql0aMrV8IBgEyZlH8VDwXFiPyMd7aSiWaz8Z52GUJLMYZkl59P5P0Ve3jmKVT2gEekEeCzgbgCMIVyxgE=",
                ECDSAUtils.sign("Message", "42"));
    }

}

