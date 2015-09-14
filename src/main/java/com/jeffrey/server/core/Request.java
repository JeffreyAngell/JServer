package com.jeffrey.server.core;

import com.jeffrey.server.util.ByteArray;
import com.jeffrey.server.util.Serializer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class Request {
    private InputStream is;
    private String method;
    private Headers h;
    private URI uri;
    private InetSocketAddress address;
    private String path;
    private String host;
    private boolean sendable;
    Serializer serializer;
    static Serializer staticSerializer = null;

    //This constructor is used internally to parse HttpExchanges
    public Request(HttpExchange e){
        sendable = false;
        if(e.getRequestHeaders() != null && e.getRequestHeaders().containsKey("Host"))
            host = e.getRequestHeaders().getFirst("Host");
        else if(e.getLocalAddress() != null)
            host = e.getLocalAddress().getHostString();
        else
            host = null;
        is = e.getRequestBody();
        method = e.getRequestMethod();
        h = e.getRequestHeaders();
        uri = e.getRequestURI();
        address = e.getRemoteAddress();
        path = e.getHttpContext().getPath();
    }

    public String getHost(){
        if(h != null && h.containsKey("Host"))
            host = h.getFirst("Host");
        else if(uri != null && uri.getHost() != null)
            host = uri.getHost();
        if(host != null && host.indexOf(":") > -1){
            host = host.substring(0, host.indexOf(":") - 1);
        }
        return host;
    }

    public InputStream getBody() {
        return is;
    }

    public String getMethod() {
        return method;
    }

    public Headers getHeaders() {
        return h;
    }

    public URI getURI() {
        return uri;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getPath(){
        return path;
    }

    //This constructor is used to send requests
    public Request(){
        sendable = true;
        serializer = staticSerializer;
    }

    public Request setURI(String s){
        uri = URI.create(s);
        path = uri.getPath();
        return this;
    }

    public Request setMethod(String s){
        method = s;
        return this;
    }

    public Request setBody(InputStream is){
        this.is = is;
        return this;
    }

    public Request setBody(Object o){
        if(serializer == null)
            throw new Serializer.NoSerializerException();
        is = new ByteArrayInputStream(serializer.serialize(o).getBytes());
        return this;
    }

    public Request setSerializer(Serializer s){
        serializer = s;
        return this;
    }

    public Request setPath(String s){
        path = s;
        return this;
    }

    public Request addHeader(String key, String value){
        if(h == null)
            h = new Headers();
        h.add(key, value);
        return this;
    }

    public static void useSerializer(Serializer s){
        staticSerializer = s;
    }

    public Response send() throws UnsendableRequest {
        if(!sendable)
            throw new UnsendableRequest("It is likely you tried to send a request to yourself.");

        if(uri == null)
            throw new UnsendableRequest("No URI specified.");
        if(method == null)
            method = "GET";

        switch(method){
            case "GET":
                return get(this);
            case "POST":
                return post(this);
            default:
                return post(this);
        }
    }

    private static Response get(Request r){
        Response response = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) r.getURI().toURL().openConnection();
            connection.setRequestMethod(r.getMethod());
            int status = connection.getResponseCode();
            byte[] bytes;
            if(status > 0 && status < 400) {
                bytes = new ByteArray(connection.getInputStream()).trim();
            }
            else
                bytes = new ByteArray(connection.getErrorStream()).trim();
            response = new Response(status, bytes);
            for(Map.Entry<String, List<String>> e: connection.getHeaderFields().entrySet()){
                response.addHeader(e.getKey(), e.getValue().get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = new Response(500, e.getMessage());
        }
        return response;
    }

    private static Response post(Request r){
        Response response = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) r.getURI().toURL().openConnection();
            connection.setRequestMethod(r.getMethod());
            byte[] body = new ByteArray(r.getBody()).trim();
            if(body.length > 0) {
                connection.setDoOutput(true);
                connection.getOutputStream().write(body);
            }
            int status = connection.getResponseCode();
            byte[] bytes;
            if(status > 0 && status < 400) {
                bytes = new ByteArray(connection.getInputStream()).trim();
            }
            else
                bytes = new ByteArray(connection.getErrorStream()).trim();
            response = new Response(status, bytes);
            for(Map.Entry<String, List<String>> e: connection.getHeaderFields().entrySet()){
                response.addHeader(e.getKey(), e.getValue().get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = new Response(500, e.getMessage());
        }
        return response;
    }

    public class UnsendableRequest extends RuntimeException {
        public UnsendableRequest(){super();}

        public UnsendableRequest(String s){
            super(s);
        }
    }
}
