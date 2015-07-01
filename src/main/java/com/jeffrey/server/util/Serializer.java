package com.jeffrey.server.util;

/**
 * Created by jeffreya on 7/1/15.
 */
public interface Serializer{
    String serialize(Object obj);
    String getContentType();

    class NoSerializerException extends RuntimeException{}
}
