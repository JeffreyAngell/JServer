package com.jeffrey.server.premade;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jeffreya on 7/2/15.
 */
public class VirtualHostHandler implements ProtoJHandler {
    Map<String, WebsiteHandler> arbitrar;
    boolean refreshing = false;

    public VirtualHostHandler() throws FileNotFoundException {
        refresh();
    }

    @Override
    public Response handle(Request r) {
        String host = r.getURI().getHost();
        if(arbitrar.containsKey(host)){
            return arbitrar.get(host).handle(r);
        }

        if(refreshing)
            return new Response(503);

        refreshing = true;
        try {
            refresh();
            host = r.getURI().getHost();
            if(arbitrar.containsKey(host)){
                return arbitrar.get(host).handle(r);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Response(500).send("Could not find hosts.conf file");
        } finally{
            refreshing = false;
        }
        return new Response(404);
    }

    public void refresh() throws FileNotFoundException {
        //I create a new variable here so as to not cause problems if a request comes in during reset
        HashMap<String, WebsiteHandler> temp = new HashMap<>();
        Scanner scanner = new Scanner(new FileInputStream("hosts.conf"));
        while(scanner.hasNextLine()){
            String[] parts = scanner.nextLine().split("\t");
            if(parts.length != 2 || temp.containsKey(parts[0]))
                throw new RuntimeException("Invalid format for hosts.conf");
            temp.put(parts[0], new WebsiteHandler(parts[1]));
        }
        scanner.close();
        arbitrar = temp;
    }
}
