package com.threadx.communication.server;

import com.threadx.communication.server.config.ServerConfig;
import junit.framework.TestCase;

public class CommunicationServerBootStrapTest extends TestCase {

    public void testStartServer() throws InterruptedException {
        CommunicationServerBootStrap communicationServerBootStrap = new CommunicationServerBootStrap(new ServerConfig(9999));
        communicationServerBootStrap.startServer();
        communicationServerBootStrap.await();
    }
}