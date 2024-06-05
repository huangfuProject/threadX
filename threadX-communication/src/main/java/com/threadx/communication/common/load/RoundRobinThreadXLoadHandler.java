package com.threadx.communication.common.load;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮训负载策略
 *
 * @author huangfukexing
 * @date 2023/4/26 13:35
 */
public class RoundRobinThreadXLoadHandler implements ThreadXLoadHandler{

    private final static AtomicInteger COUNT = new AtomicInteger(1);

    @Override
    public String load(List<String> serverAddress) {
         int selectAddressIndex =  COUNT.getAndIncrement() % serverAddress.size();
        return serverAddress.get(selectAddressIndex);
    }
}
