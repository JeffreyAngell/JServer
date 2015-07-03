package com.jeffrey.server.core;

/**
 * Created by jeffrey on 3/7/15.
 */
public interface ProtoJHandler {
    Response handle(Request r);

    static ProtoJHandler makeHandler(int status) {
        return new ProtoJHandler() {
            @Override
            public Response handle(Request r) {
                return null;
            }
        };
    }
}
