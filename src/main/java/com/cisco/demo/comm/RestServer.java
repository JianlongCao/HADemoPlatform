package com.cisco.demo.comm;

import org.restlet.Server;
import org.restlet.data.Protocol;

public class RestServer {

    private static RestServer restServer = null;
    private Server helloserver = null;

    public static RestServer Instance() {
        if(restServer == null) {
            restServer = new RestServer();
        }
        return restServer;
    }

    private RestServer() {

    }
    public void start() {
        helloserver = new Server(Protocol.HTTP, 8111, SctpaServerResource.class);
        try {
            helloserver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  stop() {
        if(helloserver != null) {
            try {
                helloserver.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
