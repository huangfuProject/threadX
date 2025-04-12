package com.threadx.communication.client;

import com.threadx.communication.client.config.ClientConfig;
import com.threadx.communication.pv.TestMessage;
import junit.framework.TestCase;

public class CommunicationClientTest extends TestCase {

    public void testAsyncSendMessage() throws Throwable {

        CommunicationClient communicationClient = new CommunicationClient(new ClientConfig("127.0.0.1", 9999, "test-server","instance"));
        Thread.sleep(3000);
        for (int i = 0; i < 10000000; i++) {
            communicationClient.asyncSendMessage(new TestMessage("哈哈哈哈哈哈" + i));
        }

        communicationClient.await();
    }
}