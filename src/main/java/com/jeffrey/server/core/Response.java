package com.jeffrey.server.core;

import com.jeffrey.server.util.ByteArray;
import com.jeffrey.server.util.DirectoryReader;
import com.jeffrey.server.util.Serializer;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.InputStream;

public class Response {
    static Serializer staticSerializer = new Serializer() {
        @Override
        public String serialize(Object obj) {
            return obj.toString();
        }

        @Override
        public String getContentType() {
            return "text/plain";
        }
    };

    int status;
    byte[] response;
    boolean stream = false;
    InputStream is = null;
    long islength;
    Headers headers = null;
    Serializer serializer;

    public Response(int i){
        this(i, (byte[]) null);
    }

    public Response(int i, String s) {
        this(i, s.getBytes());
    }

    public Response(int i, byte[] bytes){
        status = i;
        response = bytes;
        serializer = staticSerializer;
    }

    public Response send(DirectoryReader.FileObject fileObject){
        addHeader("Content-Type", fileObject.getContentType());
        return pipe(fileObject.getInputStream(), fileObject.getSize());
    }

    public Response send(Object o){
        response = serializer.serialize(o).getBytes();
        addHeader("Content-Type", serializer.getContentType());
        return this;
    }


    public Response pipe(InputStream is){
        return this.pipe(is, "");
    }

    public Response pipe(InputStream is, String s){
        ByteArray ba = null;
        try {
            ba = new ByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
            ba = new ByteArray();
        }

        response = ba.trim();
        if(response.length == 0)
            response = s.getBytes();
        return this;
    }

    public Response pipe(InputStream is, long l){
        this.is = is;
        islength = l;
        stream = true;
        return this;
    }

    public Response useSerializer(Serializer s){
        serializer = s;
        return this;
    }

    public Response setContentType(String s){
        addHeader("Content-Type", s);
        return this;
    }

    public int getStatus() {
        return status;
    }

    public long getSize() {
        if(response == null){
            response = getDefinition(status).getBytes();
            addHeader("Content-Type", "text/plain");
        }
        if(!stream) {
            long length = response.length;
            return length;
        } else{
            return islength;
        }
    }

    public byte[] getBody() {
        return response;
    }

    public boolean isStream(){
        return stream;
    }

    public InputStream getStream() {
        return is;
    }

    public Headers getHeaders(){
        return headers;
    }

    public void addHeader(String s1, String s2){
        if(headers == null)
            headers = new Headers();
        headers.add(s1, s2);
    }

    public static String getDefinition(int statusCode){
        switch(statusCode){
            case 100:
                return "Continue";
            case 101:
                return "Switching Protocols";
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 202:
                return "Accepted";
            case 203:
                return "Non-Authoritative Information";
            case 204:
                return "No Content";
            case 205:
                return "Reset Content";
            case 206:
                return "Partial Content";
            case 300:
                return "Multiple Choices";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Found";
            case 303:
                return "See Other";
            case 304:
                return "Not Modified";
            case 305:
                return "Use Proxy";
            case 307:
                return "Temporary Redirect";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 402:
                return "Payment Required";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 406:
                return "Not Acceptable";
            case 407:
                return "Proxy Authentication Required";
            case 408:
                return "Request Timeout";
            case 409:
                return "Conflict";
            case 410:
                return "Gone";
            case 411:
                return "Length Required";
            case 412:
                return "Precondition Failed";
            case 413:
                return "Request Entity Too Large";
            case 414:
                return "Request-URI Too Long";
            case 415:
                return "Unsupported Media Type";
            case 416:
                return "Requested Range Not Satisfiable";
            case 417:
                return "Expectation Failed";
            case 418:
                return "I'm a teapot";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Service Unavailable";
            case 504:
                return "Gateway Timeout";
            case 505:
                return "HTTP Version Not Supported";
        }
        return "";
    }

    public static void setSerializer(Serializer s){
        staticSerializer = s;
    }
}