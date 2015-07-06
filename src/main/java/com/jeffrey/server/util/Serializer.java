package com.jeffrey.server.util;

public interface Serializer{
    String serialize(Object obj);
    String getContentType();

    class NoSerializerException extends RuntimeException{}
}
