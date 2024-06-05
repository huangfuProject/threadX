package com.threadx.communication.common.load;

import com.threadx.communication.client.CommunicationClient;

import java.util.List;
import java.util.Set;

/**
 * threadx 负载策略处理器
 *
 * @author huangfukexing
 * @date 2023/4/26 13:32
 */
public interface ThreadXLoadHandler {

    /**
     * 负载
     *
     * @param serverAddress 备选地址
     * @return 负载好的地址
     */
    String load(List<String> serverAddress);
}
