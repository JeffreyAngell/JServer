package demo.JServerDemo.handler;

import com.jeffrey.server.core.JHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

public class BadHandler implements JHandler {
    @Override
    public Response handle(Request r) {
        return new Response(500, "Something went wrong");
    }
}
