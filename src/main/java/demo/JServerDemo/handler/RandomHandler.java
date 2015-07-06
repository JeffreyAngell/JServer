package demo.JServerDemo.handler;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

import java.util.Random;

/**
 * Created by jeffrey on 3/31/15.
 */
public class RandomHandler implements ProtoJHandler {
    @Override
    public Response handle(Request r) {
        Random rand = new Random();
        if(rand.nextDouble() < .5){
            return new GoodHandler().handle(r);
        } else{
            return new BadHandler().handle(r);
        }
    }
}
