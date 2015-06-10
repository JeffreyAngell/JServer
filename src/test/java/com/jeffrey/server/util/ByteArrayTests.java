package com.jeffrey.server.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteArrayTests {
    @Test
    public void byteArrayTest() throws IOException {
        ByteArray ba = new ByteArray();
        byte[] bytes = new byte[32];
        ByteArrayInputStream bais = new ByteArrayInputStream("I'm a dorky dork dork. I like to".getBytes());
        bais.read(bytes);
        ba.add(bytes);
        byte[] checkpoint = ba.trim();
        Assert.assertArrayEquals(checkpoint, bytes);
        Assert.assertEquals(new String(checkpoint), "I'm a dorky dork dork. I like to");
        ba.add(" program!".getBytes());
        Assert.assertEquals(new String(ba.trim()), "I'm a dorky dork dork. I like to program!");
        Assert.assertEquals(ba.trim().length, 32 + 9);
    }
}
