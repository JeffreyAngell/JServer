package com.jeffrey.server.premade;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;
import com.jeffrey.server.util.DirectoryReader;
import com.jeffrey.server.util.WebsiteDirectoryReader;

import java.io.File;
import java.io.FileNotFoundException;

public class WebsiteHandler implements ProtoJHandler {
    WebsiteDirectoryReader reader;
    public WebsiteHandler(String s) throws FileNotFoundException{
        reader = new WebsiteDirectoryReader(new File(s));
    }

    @Override
    public Response handle(Request r) {
        String offset = r.getPath().equals("/") ? r.getURI().getPath() : r.getURI().getPath().replaceFirst(r.getPath(), "");
        DirectoryReader.FileObject f;
        try {
            f = reader.get(offset);
            return new Response(200).send(f);
        } catch (FileNotFoundException e) {
            f = reader.getError();
            return new Response(404).send(f);
        }
    }
}
