package com.jeffrey.server.core;

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
            JServer s = new JServer(9000);
            s.register("/asdf", new JHandler() {
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
        JServer s;
        try {
            s = new JServer(8080);
            s.start();
            s.register("/asdf", new JHandler() {
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
            return;
        }

        try{
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/asdf").openConnection();
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