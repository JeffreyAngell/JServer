package com.jeffrey.server.core;

import com.sun.net.httpserver.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by jeffreya on 7/1/15.
 */
public class RequestTests {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void canSendRequestTest(){
        Response r = new Request().setURI("http://www.google.com").send();
        Assert.assertTrue(r.getStatus() < 400); //I don't know what the response will be, but it should not be an error
        String s = new String(r.getBody());
    }

    @Test
    public void cannotSendRequestFromMeTest(){
        thrown.expect(Request.UnsendableRequest.class);
        Response r = new Request(new MockHttpExchange()).send();
    }



    private class MockHttpExchange extends HttpExchange{
        @Override
        public Headers getRequestHeaders() {
            return null;
        }

        @Override
        public Headers getResponseHeaders() {
            return null;
        }

        @Override
        public URI getRequestURI() {
            return null;
        }

        @Override
        public String getRequestMethod() {
            return null;
        }

        @Override
        public HttpContext getHttpContext() {
            return new HttpContext(){
                @Override
                public HttpHandler getHandler() {
                    return null;
                }

                @Override
                public void setHandler(HttpHandler httpHandler) {

                }

                @Override
                public String getPath(){
                    return null;
                }

                @Override
                public HttpServer getServer() {
                    return null;
                }

                @Override
                public Map<String, Object> getAttributes() {
                    return null;
                }

                @Override
                public List<Filter> getFilters() {
                    return null;
                }

                @Override
                public Authenticator setAuthenticator(Authenticator authenticator) {
                    return null;
                }

                @Override
                public Authenticator getAuthenticator() {
                    return null;
                }
            };
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getRequestBody() {
            return null;
        }

        @Override
        public OutputStream getResponseBody() {
            return null;
        }

        @Override
        public void sendResponseHeaders(int i, long l) throws IOException {

        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public int getResponseCode() {
            return 0;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public void setAttribute(String s, Object o) {

        }

        @Override
        public void setStreams(InputStream inputStream, OutputStream outputStream) {

        }

        @Override
        public HttpPrincipal getPrincipal() {
            return null;
        }
    }
}
