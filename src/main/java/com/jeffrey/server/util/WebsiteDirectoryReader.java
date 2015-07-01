package com.jeffrey.server.util;

import com.jeffrey.server.core.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by jeffreya on 6/30/15.
 */
public class WebsiteDirectoryReader extends DirectoryReader {
    String errorLocation = "/error.html";

    public WebsiteDirectoryReader(File file) throws FileNotFoundException{
        super(file);
    }

    @Override
    public FileObject get(String offset) throws FileNotFoundException{
        String location = base + offset;
        File f = new File(location);
        if(f.isDirectory()){
            f = new File(location + "/index.html");
        }
        return new FileObject(f.length(), new FileInputStream(f), ContentType.getContentType(f));
    }

    public FileObject getError(){
        File error = new File(base + errorLocation);
        try {
            return new FileObject(error.length(), new FileInputStream(error), ContentType.getContentType(error));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Java code didn't work.");
        }
    }

    public void setErrorLocation(String s) throws FileNotFoundException{
        File f = new File(base + s);
        if(!f.exists())
            throw new FileNotFoundException();
        if(!f.isFile())
            throw new DirectoryNotFileException();
        errorLocation = s;
    }
    public class DirectoryNotFileException extends FileNotFoundException{}
}
