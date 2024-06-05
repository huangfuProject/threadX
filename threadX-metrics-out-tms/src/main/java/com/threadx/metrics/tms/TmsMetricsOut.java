package com.threadx.metrics.tms;

import cn.hutool.core.bean.BeanUtil;
import com.threadx.communication.client.ConnectionManager;
import com.threadx.communication.client.config.ClientConfig;
import com.threadx.communication.common.agreement.packet.ThreadPoolCollectMessage;
import com.threadx.communication.common.agreement.packet.ThreadPoolTaskCollectMessage;
import com.threadx.communication.common.load.RoundRobinThreadXLoadHandler;
import com.threadx.description.context.AgentContext;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.metrics.ThreadPoolExecutorData;
import com.threadx.metrics.ThreadTaskExecutorData;
import com.threadx.metrics.api.MetricsOutApi;
import com.threadx.metrics.tms.config.ThreadXMetricsTmsPropertiesEnum;
import com.threadx.metrics.tms.handler.ThreadPoolParamUpdateHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ************************************************<br/>
 * threadX 指标收集服务类型<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/9 14:55
 */
public class TmsMetricsOut implements MetricsOutApi {

    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(TmsMetricsOut.class);

    public static final String METRICS_OUT = "tms";

    @Override
    public void init() {
        ConnectionManager.setLoadHandler(new RoundRobinThreadXLoadHandler());
        String addressStr = AgentContext.getAgentPackageDescription().getEnvProperties().getProperty(ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_ADDRESS.getKey(), ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_ADDRESS.getDefaultValue());
        if (ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_ADDRESS.getDefaultValue().equalsIgnoreCase(addressStr)) {
            logger.error("Indicator collector initialization failed. Please check the configuration: {}", ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_ADDRESS.getKey());
            throw new RuntimeException("Indicator collector initialization failed. Please check the configuration.");
        }

        //先分隔所有的节点
        String[] remoteNodeAddressArray = addressStr.split(",");
        List<ClientConfig> clientConfigs = Arrays.stream(remoteNodeAddressArray).map(remoteNodeAddress -> {
            String[] address = remoteNodeAddress.split(":");
            String host = address[0];
            String port = address[1];
            ClientConfig clientConfig = new ClientConfig(host, Integer.parseInt(port), AgentContext.getServerName(), AgentContext.getInstanceName());
            clientConfig.addHandler("threadPoolParamUpdateHandler", new ThreadPoolParamUpdateHandler());
            return clientConfig;
        }).collect(Collectors.toList());
        //连接服务端
        ConnectionManager.connection(clientConfigs);

    }

    @Override
    public void outThreadPoolMetricsData(ThreadPoolExecutorData metricsData) {
        ThreadPoolCollectMessage threadPoolCollectMessage = new ThreadPoolCollectMessage();
        BeanUtil.copyProperties(metricsData, threadPoolCollectMessage);
        threadPoolCollectMessage.setThreadPoolObjectId(metricsData.getObjectId());
        ConnectionManager.asyncSendMessageLoad(threadPoolCollectMessage);
    }

    @Override
    public void outThreadTaskMetricsData(ThreadTaskExecutorData metricsData) {
        ThreadPoolTaskCollectMessage threadPoolTaskCollectMessage = new ThreadPoolTaskCollectMessage();
        BeanUtil.copyProperties(metricsData, threadPoolTaskCollectMessage);
        ConnectionManager.asyncSendMessageLoad(threadPoolTaskCollectMessage);
    }


    @Override
    public void destroy() {
        ConnectionManager.closeAll();
    }

    @Override
    public String getMetricsName() {
        return METRICS_OUT;
    }

}
