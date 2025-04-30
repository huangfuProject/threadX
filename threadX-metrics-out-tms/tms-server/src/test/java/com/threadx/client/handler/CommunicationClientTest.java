package com.threadx.client.handler;

import com.threadx.communication.client.CommunicationClient;
import com.threadx.communication.client.config.ClientConfig;
import com.threadx.metrics.tms.handler.client.ThreadPoolIndicatorReportingHandler;
import com.threadx.metrics.tms.handler.client.ThreadPoolTaskIndicatorReportingHandler;
import junit.framework.TestCase;

public class CommunicationClientTest extends TestCase {

    public void testAsyncSendMessage() throws Throwable {

        ClientConfig instance = new ClientConfig("10.0.55.58", 9876, "test-server", "instance");
        instance.addHandler("print1", new ThreadPoolIndicatorReportingHandler());
        instance.addHandler("print2", new ThreadPoolTaskIndicatorReportingHandler());
        CommunicationClient communicationClient = new CommunicationClient(instance);
        Thread.sleep(5000);
        communicationClient.await();
    }
}