package com.jeffrey.server.premade;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;
import com.jeffrey.server.util.ByteArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Created by jeffreya on 7/2/15.
 */
public class VirtualHostHandlerTests {

    private String[] sites = new String[]{"asdf.site1.com", "qwer.site2.net", "site3.xyz", "site1.com"};

    @Before
    public void makeFiles(){
        for(String s: sites){
            makeSite(s);
        }
        try {
            File f = new File("hosts.conf");
            if(f.exists())
                f.delete();
            FileOutputStream f1 = new FileOutputStream("hosts.conf");
            for(String s: sites){
                f1.write((s + "\t" + s + "\n").getBytes());
            }
            f1.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating files");
            Assert.fail();
        }
    }

    private void makeSite(String s){
        File f = new File(s);
        if(f.exists() && f.isDirectory())
            recursiveDelete(f);
        else if(f.exists())
            f.delete();
        boolean success = f.mkdir();
        if(success) {
            try {
                FileOutputStream f1 = new FileOutputStream(s + "/index.html");
                f1.write(s.getBytes());
                f1.close();
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
        for(String s: sites) {
            File f = new File(s);
            if (f.isDirectory())
                recursiveDelete(f);
        }
        new File("hosts.conf").delete();
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
    public void simpleVerify(){
        ProtoJHandler h = null;
        try {
            h = new VirtualHostHandler();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }
        for(String s: sites){
            Request req = new Request().setURI("http://" + s);
            Response res = h.handle(req);
            try {
                Assert.assertArrayEquals(s.getBytes(), new ByteArray(res.getStream()).trim());
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
        for(String s: sites){
            Request req = new Request().setURI("http://" + s + "/index.html");
            Response res = h.handle(req);
            try {
                Assert.assertArrayEquals(s.getBytes(), new ByteArray(res.getStream()).trim());
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    @Test
    public void refreshVerify(){
        ProtoJHandler h = null;
        try {
            h = new VirtualHostHandler();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Request req = new Request().setURI("http://does.not.exist/index.html");
        Response res = h.handle(req);
        Assert.assertEquals(404, res.getStatus());
        try(FileWriter fw = new FileWriter("hosts.conf")){
            fw.write("exists.now\texists.now\n");
            makeSite("exists.now");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        req = new Request().setURI("http://exists.now");
        res = h.handle(req);
        try {
            Assert.assertArrayEquals("exists.now".getBytes(), new ByteArray(res.getStream()).trim());
            File f = new File("exists.now");
            if(f.exists())
                recursiveDelete(f);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
