package com.jeffrey.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
* Created by jeffrey on 3/7/15.
*/
public class Response {
    int status;
    byte[] response;
    boolean stream = false;
    InputStream is = null;
    long islength;
    Map<Integer, String> responses;
    Headers headers = null;

    public Response(int i){
        status = i;
        response = null;
        responses = new HashMap<>();
        responses.put(200, "OK");
        responses.put(201, "Created");
        responses.put(206, "Partial content");
        responses.put(400, "Bad request");
        responses.put(401, "Unauthorized");
        responses.put(403, "Forbidden");
        responses.put(404, "Not found");
        responses.put(405, "Method not implemented");
        responses.put(409, "Conflict");
        responses.put(500, "Internal server error");
        responses.put(503, "Service temporarily unavailable");
    }

    public Response(int i, String s) {
        status = i;
        response = s.getBytes();
    }

    public Response(int i, byte[] bytes){
        status = i;
        response = bytes;
    }

    public Response send(Object o){
        response = new Gson().toJson(o).getBytes();
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


    public int getStatus() {
        return status;
    }

    public long getSize() {
        if(response == null){
            response = responses.get(status).getBytes();
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
}
