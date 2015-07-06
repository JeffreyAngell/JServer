package com.jeffrey.server.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class WebsiteDirectoryReaderTests {
    static String good = "This is a page. qwerqwerqwer";
    static String error = "This is the error page. asdfasdfasdf";

    @Before
    public void setup(){
        File f = new File("tempdir");
        if(f.exists() && f.isDirectory())
            recursiveDelete(f);
        else if(f.exists())
            f.delete();
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
    public void verifyBasicPassthrough(){
        try {
            WebsiteDirectoryReader reader = new WebsiteDirectoryReader(new File("tempdir"));

            DirectoryReader.FileObject f;
            try {
                f = reader.get("/badurl");
                Assert.fail();
            } catch(FileNotFoundException e){
                f = reader.getError();
                Assert.assertArrayEquals(new ByteArray(f.getInputStream()).trim(), error.getBytes());
                Assert.assertEquals(f.getContentType(), "text/html");
            }

            f = reader.get("/error.html");
            Assert.assertArrayEquals(error.getBytes(), new ByteArray(f.getInputStream()).trim());;
            Assert.assertEquals(f.getContentType(), "text/html");

            f = reader.get("");
            Assert.assertArrayEquals(new ByteArray(f.getInputStream()).trim(), good.getBytes());
            Assert.assertEquals(f.getContentType(), "text/html");

            f = reader.get("/");
            Assert.assertArrayEquals(new ByteArray(f.getInputStream()).trim(), good.getBytes());
            Assert.assertEquals(f.getContentType(), "text/html");

            f = reader.get("/index.html");
            Assert.assertArrayEquals(new ByteArray(f.getInputStream()).trim(), good.getBytes());
            Assert.assertEquals(f.getContentType(), "text/html");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
