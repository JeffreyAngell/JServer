package com.jeffrey.server.premade;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VirtualHostHandler implements ProtoJHandler {
    Map<String, WebsiteHandler> arbitrar;
    String location;
    boolean refreshing = false;

    public VirtualHostHandler() throws FileNotFoundException {
        this("hosts.conf");
    }

    public VirtualHostHandler(String conf) throws FileNotFoundException{
        location = conf;
        refresh();
    }

    @Override
    public Response handle(Request r) {
        String host = r.getHost();
        if(arbitrar.containsKey(host)){
            return arbitrar.get(host).handle(r);
        }

        if(refreshing)
            return new Response(503);

        refreshing = true;
        try {
            refresh();
            if(arbitrar.containsKey(host)){
                return arbitrar.get(host).handle(r);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Response(500).send(String.format("Could not find conf file %s\n", location));
        } finally{
            refreshing = false;
        }
        return new Response(404);
    }

    public void refresh() throws FileNotFoundException {
        //I create a new variable here so as to not cause problems if a request comes in during reset
        HashMap<String, WebsiteHandler> temp = new HashMap<>();
        Scanner scanner = new Scanner(new FileInputStream(location));
        while(scanner.hasNextLine()){
            String[] parts = scanner.nextLine().split("\\s+");
            if(parts.length != 2 || temp.containsKey(parts[0]))
                throw new RuntimeException(String.format("Invalid format for conf file %s\n", location));
            temp.put(parts[0], new WebsiteHandler(parts[1]));
        }
        scanner.close();
        arbitrar = temp;
    }
}
