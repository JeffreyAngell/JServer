package com.jeffrey.server.premade;

import com.jeffrey.server.core.ProtoJHandler;
import com.jeffrey.server.core.Request;
import com.jeffrey.server.core.Response;

public class JHandlerBuilder {
    int status = 0;
    private JHandlerBuilder(int i){
        status = i;
    }

    public static JHandlerBuilder status(int i) {
        return new JHandlerBuilder(i);
    }

    public ProtoJHandler build() {
        if(status == 0)
            throw new InvalidJHandlerException("No response code set");
        return new ProtoJHandler() {
            @Override
            public Response handle(Request r) {
                return new Response(status);
            }
        };
    }

    public class InvalidJHandlerException extends RuntimeException{
        public InvalidJHandlerException(String s){
            super(s);
        }
    }
}
