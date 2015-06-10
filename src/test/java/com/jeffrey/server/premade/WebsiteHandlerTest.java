package com.jeffrey.server.premade;

import com.jeffrey.server.core.JServer;
import com.jeffrey.server.core.Response;
import com.jeffrey.server.util.ByteArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class WebsiteHandlerTest {
    static Random rand;
    public WebsiteHandlerTest(){
        rand = new Random();
    }
    static String good = "This is a page. qwerqwerqwer";
    static String error = "This is the error page. asdfasdfasdf";

    @Before
    public void setup(){
        File f = new File("tempdir");
        boolean success = f.mkdir();
        if(success) {
            try {
                FileOutputStream f1 = new FileOutputStream("tempdir/index.html");
                f1.write(good.getBytes());
                f1.close();

                FileOutputStream f2 = new FileOutputStream("tempdir/error.html");
                f2.write(error.getBytes());
                f2.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error creating files");
                Assert.fail();
            }
        } else {
            System.out.println("Error creating files");
            Assert.fail();
        }
    }

    @After
    public void teardown() {
        File f = new File("tempdir");
        if(f.isDirectory())
            recursiveDelete(f);
    }

    public void recursiveDelete(File file){
        for(File f: file.listFiles()){
            if(f.isDirectory()){
                recursiveDelete(f);
            } else if(f.isFile()){
                f.delete();
            }
        }
        file.delete();
    }


    @Test
    public void websiteAtLocationChecker(){
        try{
            JServer server = new JServer(0);
            int port = server.getPort();
            String url = "http://localhost:" + String.valueOf(port) + "/a";
            server.register("/a", new WebsiteHandler("tempdir"));
            server.start();

            Response response = get(url + "/badurl");
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), error);
            Assert.assertEquals(response.getStatus(), 404);

            response = get(url + "/error.html");
            Assert.assertEquals(error, new String(response.getBody(), "UTF-8"));
            Assert.assertEquals(response.getStatus(), 200);

            response = get(url);
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), good);
            Assert.assertEquals(response.getStatus(), 200);

            response = get(url + "/");
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), good);
            Assert.assertEquals(response.getStatus(), 200);

            response = get(url + "/index.html");
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), good);
            Assert.assertEquals(response.getStatus(), 200);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void websiteAtRootChecker(){
        try{
            JServer server = new JServer(0);
            int port = server.getPort();
            String url = "http://localhost:" + String.valueOf(port);

            server.register("/", new WebsiteHandler("tempdir"));
            server.start();

            Response response = get(url + "/badurl");
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), error);
            Assert.assertEquals(response.getStatus(), 404);

            response = get(url + "/error.html");
            Assert.assertEquals(error, new String(response.getBody(), "UTF-8"));
            Assert.assertEquals(response.getStatus(), 200);

            response = get(url);
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), good);
            Assert.assertEquals(response.getStatus(), 200);

            response = get(url + "/");
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), good);
            Assert.assertEquals(response.getStatus(), 200);

            response = get(url + "/index.html");
            Assert.assertEquals(new String(response.getBody(), "UTF-8"), good);
            Assert.assertEquals(response.getStatus(), 200);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public Response get(String url){
        Response r = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            byte[] bytes;
            if(status > 0 && status < 400) {
                bytes = new ByteArray(connection.getInputStream()).trim();
            }
            else
                bytes = new ByteArray(connection.getErrorStream()).trim();
            r = new Response(status, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        return r;
    }
}
