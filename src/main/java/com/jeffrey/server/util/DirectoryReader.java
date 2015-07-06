package com.jeffrey.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public abstract class DirectoryReader {
    String base;

    public DirectoryReader(File directory) throws FileNotFoundException{
        if(!directory.isDirectory() || !directory.canRead()){
            throw new FileNotFoundException("Please check that directory exists and you have read permissions");
        }
        base = directory.getAbsolutePath();
    }

    public abstract FileObject get(String offset) throws FileNotFoundException;

    public class FileObject{
        private long size;
        private InputStream is;
        private String contentType;

        public FileObject(long s, InputStream stream, String type){
            size = s;
            is = stream;
            contentType = type;
        }

        public long getSize(){
            return size;
        }
        public InputStream getInputStream(){
            return is;
        }
        public String getContentType(){
            return contentType;
        }
    }
}
