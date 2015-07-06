package demo.JServerDemo.handler;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

public class BadHandler implements ProtoJHandler {
    @Override
    public Response handle(Request r) {
        return new Response(500, "Something went wrong");
    }
}
