package com.threadx.metrics.server.init;

import cn.hutool.extra.spring.SpringUtil;
import com.threadx.communication.common.handlers.ThreadXChannelInboundHandler;
import com.threadx.communication.server.CommunicationServerBootStrap;
import com.threadx.communication.server.config.ServerConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通讯初始化
 *
 * @author huangfukexing
 * @date 2023/4/12 16:22
 */
@Component
@DependsOn({"threadPoolDataConsumer","threadTaskDataConsumer"})
public class CommunicationInit implements InitializingBean, DisposableBean {

    @Value("${threadx.communication.port}")
    private int threadxCommunicationPort;


    private CommunicationServerBootStrap communicationServerBootStrap;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ThreadXChannelInboundHandler> beansOfType = SpringUtil.getBeansOfType(ThreadXChannelInboundHandler.class);
        ServerConfig serverConfig = new ServerConfig(threadxCommunicationPort);
        beansOfType.forEach(serverConfig::addChannelInboundHandler);
        communicationServerBootStrap = new CommunicationServerBootStrap(serverConfig);
        communicationServerBootStrap.startServer();
    }

    @Override
    public void destroy() throws Exception {
        if(communicationServerBootStrap != null) {
            communicationServerBootStrap.close();
        }
    }
}
