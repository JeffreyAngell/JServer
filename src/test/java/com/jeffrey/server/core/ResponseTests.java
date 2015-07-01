package com.jeffrey.server.core;

import com.jeffrey.server.util.ByteArray;
import com.jeffrey.server.util.Serializer;
import org.junit.Assert;
import org.junit.Test;

public class ResponseTests {
    @Test
    public void canUseSerializer(){
        CountSerializer first = new CountSerializer();
        CountSerializer second = new CountSerializer();
        Response.setSerializer(first);
        new Response(200).send("A thing");
        new Response(520).useSerializer(second).send(new ByteArray());
        Assert.assertEquals(1, first.getCalled());
        Assert.assertEquals(1, second.getCalled());
        new Response(301).send(new Object());
        Assert.assertEquals(2, first.getCalled());
    }

    class CountSerializer implements Serializer {
        private int called = 0;
        @Override
        public String serialize(Object obj) {
            called++;
            return "";
        }

        @Override
        public String getContentType() {
            return "";
        }

        public int getCalled(){
            return called;
        }
    }
}
