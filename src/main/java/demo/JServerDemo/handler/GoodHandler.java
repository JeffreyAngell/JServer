package demo.JServerDemo.handler;

import com.jeffrey.server.core.JHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

public class GoodHandler implements JHandler {
    @Override
    public Response handle(Request r) {
        return new Response(200).pipe(r.getBody(), "Good!");
    }
}
