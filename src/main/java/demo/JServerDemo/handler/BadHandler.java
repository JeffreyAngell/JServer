package demo.JServerDemo.handler;

import main.java.com.jeffrey.server.core.JHandler;
import main.java.com.jeffrey.server.core.Request;
import main.java.com.jeffrey.server.core.Response;

/**
 * Created by jeffrey on 3/31/15.
 */
public class BadHandler implements JHandler {
    @Override
    public Response handle(Request r) {
        return new Response(500, "Something went wrong");
    }
}
