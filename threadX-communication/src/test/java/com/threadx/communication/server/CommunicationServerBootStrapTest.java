package com.threadx.communication.server;

import com.threadx.communication.handler.TestMessageHandler;
import com.threadx.communication.server.config.ServerConfig;
import junit.framework.TestCase;

public class CommunicationServerBootStrapTest extends TestCase {

    public void testStartServer() throws InterruptedException {
        ServerConfig serverConfig = new ServerConfig(9999);
        serverConfig.addChannelInboundHandler("testMessage", new TestMessageHandler());
        CommunicationServerBootStrap communicationServerBootStrap = new CommunicationServerBootStrap(serverConfig);
        communicationServerBootStrap.startServer();
        communicationServerBootStrap.await();
    }
}