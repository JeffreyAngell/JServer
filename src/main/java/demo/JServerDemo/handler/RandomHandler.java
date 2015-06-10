package demo.JServerDemo.handler;

import main.java.com.jeffrey.server.core.JHandler;
import main.java.com.jeffrey.server.core.Request;
import main.java.com.jeffrey.server.core.Response;

import java.util.Random;

/**
 * Created by jeffrey on 3/31/15.
 */
public class RandomHandler implements JHandler {
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
