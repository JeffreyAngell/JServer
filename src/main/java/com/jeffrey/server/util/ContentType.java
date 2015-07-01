package com.jeffrey.server.util;

import java.io.File;

/**
 * Created by jeffreya on 6/30/15.
 */
public class ContentType {
    public static String getContentType(File f){
        switch(f.getName().substring(f.getName().indexOf(".") + 1)){
            case "html":
                return "text/html";
            case "jpeg":
                return "image/jpeg";
            case "mp4":
                return "audio/mp4";
            case "xml":
                return "application/xml";
        }
        return "unknown";
    }
}
