package com.jeffrey.server.util;

import java.io.IOException;
import java.io.InputStream;

public class ByteArray {
    private byte[] bytes;
    private int length = 512;
    private int read;

    public ByteArray(){
        bytes = new byte[length];
        read = 0;
    }

    public ByteArray(InputStream is) throws IOException {
        this();
        byte[] t = new byte[2048];
        int read = 0;
        while((read = is.read(t)) > 0){
            this.add(t, read);
        }
    }

    public void add(byte[] bytes){
        add(bytes, bytes.length);
    }

    public void add(byte[] bytes, int size){
        while(size + read > length){
            length *= 2;
            byte[] newBytes = new byte[length];
            for(int i = 0; i < read; i++){
                newBytes[i] = this.bytes[i];
            }
            this.bytes = newBytes;
        }
        for(int i = 0; i < size; i++){
            this.bytes[read++] = bytes[i];
        }
    }

    public byte[] trim(){
        byte[] bytes = new byte[read];
        for(int i = 0; i < read; i++)
            bytes[i] = this.bytes[i];
        return bytes;
    }
}
