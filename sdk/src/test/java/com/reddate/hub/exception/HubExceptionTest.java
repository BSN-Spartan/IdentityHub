package com.reddate.hub.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.reddate.hub.sdk.exception.HubException;
import org.junit.Test;

public class HubExceptionTest {
    @Test
    public void testConstructor() {
        HubException actualHubException = new HubException(1, "An error occurred");

        assertNull(actualHubException.getCause());
        assertEquals("com.hub.exception.HubException: An error occurred", actualHubException.toString());
        assertEquals(0, actualHubException.getSuppressed().length);
        assertEquals("An error occurred", actualHubException.getMessage());
        assertEquals("An error occurred", actualHubException.getLocalizedMessage());
    }

    @Test
    public void testConstructor2() {
        HubException actualHubException = new HubException(9999,"An error occurred");
        assertNull(actualHubException.getCause());
        assertEquals("com.hub.exception.HubException: An error occurred", actualHubException.toString());
        assertEquals(0, actualHubException.getSuppressed().length);
        assertEquals("An error occurred", actualHubException.getMessage());
        assertEquals("An error occurred", actualHubException.getLocalizedMessage());
    }
}

