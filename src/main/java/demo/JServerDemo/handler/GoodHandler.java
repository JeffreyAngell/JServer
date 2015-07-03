package demo.JServerDemo.handler;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

public class GoodHandler implements ProtoJHandler {
    @Override
    public Response handle(Request r) {
        return new Response(200).pipe(r.getBody(), "Good!");
    }
}
