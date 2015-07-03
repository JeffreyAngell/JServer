package com.jeffrey.server.integration;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.ProtoJServer;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;
import com.jeffrey.server.util.ByteArray;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BasicResponseTest {
    @Test
    public void codeCompiles(){
        try {
            ProtoJServer s = new ProtoJServer(0);
            s.register("/asdf", new ProtoJHandler() {
                @Override
                public Response handle(Request r) {
                    if(r.getMethod().equals("GET")) {
                        Scanner scanner = new Scanner(r.getBody());
                        scanner.useDelimiter("\\A");
                        if(!scanner.hasNext())
                            return new Response(200);
                        else
                            return new Response(200, scanner.next());
                    }
                    if(r.getMethod().equals("POST")){
                        return new Response(500).pipe(r.getBody());
                    }
                    return new Response(405);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }
    }

    @Test
    public void serverBasicsWorkTest(){
        ProtoJServer s;
        int port = 0;
        try {
            s = new ProtoJServer(0);
            s.start();
            port = s.getPort();
            s.register("/asdf", new ProtoJHandler() {
                @Override
                public Response handle(Request r) {
                    if(r.getMethod().equals("POST")){
                        return new Response(200).pipe(r.getBody());
                    }
                    return new Response(405);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }

        try{
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:" + String.valueOf(port) + "/asdf").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            String string = "This is a test of the echo server.";

            connection.getOutputStream().write(string.getBytes());
            connection.getOutputStream().close();
            InputStream is = connection.getInputStream();
            ByteArray ba = new ByteArray();
            while(is.available() > 0){
                byte[] t = new byte[is.available()];
                is.read(t);
                ba.add(t);
            }
            Assert.assertEquals(string, new String(ba.trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        s.stop();
    }
}