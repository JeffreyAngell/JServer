package demo.JServerDemo.handler;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

import java.util.Scanner;

public class ElseHandler implements ProtoJHandler {
    @Override
    public Response handle(Request r) {
        return new Response(200, format(r));
    }

    public String format(Request r){
        StringBuilder s = new StringBuilder();
        Scanner scanner = new Scanner(r.getBody());
        scanner.useDelimiter("\\A");
        s.append("Method: ").append(r.getMethod()).append("\n");
        if(scanner.hasNext())
            s.append("Body: ").append(scanner.next()).append("\n");
        s.append("Headers: ").append(r.getHeaders()).append("\n");
        s.append("Remote address: ").append(r.getAddress()).append("\n");
        return s.toString();
    }
}
