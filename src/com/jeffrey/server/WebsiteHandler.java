package com.jeffrey.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by jeffrey on 4/10/15.
 */
public class WebsiteHandler implements JHandler {
    String base = "";
    public WebsiteHandler(String s) throws FileNotFoundException{
        base = s;
        File d = new File(s);
        if(!d.isDirectory() || !d.canRead()){
            throw new FileNotFoundException("Please check that directory exists and you have read permissions");
        }
    }

    @Override
    public Response handle(Request r) {
        String location = base + r.getURI().getPath().replaceFirst(r.getPath(), "");
        File f = new File(location);
        if(f.isDirectory()){
            f = new File(location + "/index.html");
        }
        if(!f.isFile()){
            File error = new File(base + "/error.html");
            if(error.isFile() && error.canRead())
                try {
                    return new Response(404).pipe(new FileInputStream(error), error.length());
                } catch (FileNotFoundException e) {
                    return new Response(500);
                }
            return new Response(404);
        }
        if(!f.canRead()){
            return new Response(403);
        }
        try {
            return new Response(200).pipe(new FileInputStream(f), f.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Response(500);
        }
    }
}
