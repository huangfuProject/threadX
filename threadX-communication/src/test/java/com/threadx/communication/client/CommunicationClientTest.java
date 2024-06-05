package com.threadx.communication.client;

import com.threadx.communication.client.config.ClientConfig;
import com.threadx.communication.common.agreement.packet.ThreadPoolCollectMessage;
import junit.framework.TestCase;

public class CommunicationClientTest extends TestCase {

    public void testAsyncSendMessage() throws Throwable {

        CommunicationClient communicationClient = new CommunicationClient(new ClientConfig("10.0.55.81", 9999, "test-server","instance"));
        ThreadPoolCollectMessage message = new ThreadPoolCollectMessage();
        message.setThreadPoolName("testsdtsdtstd");
        message.setThreadPoolGroupName("sdsdsda#32");
        communicationClient.asyncSendMessage(message);
        communicationClient.close();
    }
}