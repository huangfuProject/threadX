package com.threadx.communication.client;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.threadx.communication.client.config.ClientConfig;
import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.load.ThreadXLoadHandler;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

/**
 * 连接管理器
 *
 * @author huangfukexing
 * @date 2023/4/25 15:00
 */
public class ConnectionManager {
    private static ThreadXLoadHandler loadHandler = null;

    static final Logger logger = Logger.getGlobal();

    private final static ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    private final static Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    private final static Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();
    private final static ReentrantLock lock = new ReentrantLock();

    private final static AtomicBoolean IS_START = new AtomicBoolean(false);
    private static ScheduledFuture<?> scheduledFuture = null;
    private static ScheduledFuture<?> checkConnection = null;

    private final static ScheduledThreadPoolExecutor RE_CONNECTION_POOL = new ScheduledThreadPoolExecutor(2, r -> {
        Thread thread = new Thread(r);
        thread.setName("re-Connection-Pool");
        return thread;
    });
    /**
     * 活跃的连接
     */
    private static final Map<String, CommunicationClient> ACTIVE_CONNECTION = new ConcurrentHashMap<>();

    /**
     * 失效的连接
     */
    private static final Map<String, CommunicationClient> FAILURE_CONNECTION = new ConcurrentHashMap<>();


    /**
     * 连接对象
     *
     * @param clientConfigs 客户端配置
     */
    public static void connection(List<ClientConfig> clientConfigs) {
        for (ClientConfig clientConfig : clientConfigs) {
            new CommunicationClient(clientConfig);
        }
    }

    /**
     * 关闭所有的连接
     */
    public static void closeAll() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }

        if (checkConnection != null) {
            checkConnection.cancel(false);
        }
        //开始筛选
        ACTIVE_CONNECTION.forEach((k, v) -> {
            if (v.communicationStatus()) {
                closeConnection(k);
            }
        });

        //开始筛选
        FAILURE_CONNECTION.forEach((k, v) -> {
            if (v.communicationStatus()) {
                closeConnection(k);
            }
        });

    }


    /**
     * 关闭连接
     *
     * @param serverAddress 服务地址
     */
    public static void closeConnection(String serverAddress) {
        CommunicationClient removeActive = ACTIVE_CONNECTION.remove(serverAddress);
        if(removeActive != null) {
            removeActive.close();
        }

        CommunicationClient removeFailure = FAILURE_CONNECTION.remove(serverAddress);
        if(removeFailure != null) {
            removeFailure.close();
        }

    }

    /**
     * 新建一个有效的连接
     *
     * @param communicationClient 通信客户端
     */
    public static void newActiveConnection(CommunicationClient communicationClient) {
        WRITE_LOCK.lock();
        try {
            ACTIVE_CONNECTION.put(communicationClient.getServerAddress(), communicationClient);
            FAILURE_CONNECTION.remove(communicationClient.getServerAddress());
        } finally {
            WRITE_LOCK.unlock();
        }

    }

    /**
     * 将一个活跃的连接变为不可用，等待重连
     *
     * @param communicationClient 通信客户端
     */
    public static void confinementConnection(CommunicationClient communicationClient) {
        WRITE_LOCK.lock();
        try {
            CommunicationClient remove = ACTIVE_CONNECTION.remove(communicationClient.getServerAddress());
            if(remove != null) {
                remove.close();
            }

            FAILURE_CONNECTION.put(communicationClient.getServerAddress(), communicationClient);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * 异步发送消息 加载消息
     *
     * @param message 消息对象
     */
    public static void asyncSendMessageLoad(Message message) {
        Map<String, CommunicationClient> allActiveConnection = ConnectionManager.getAllActiveConnection();
        List<String> address = new ArrayList<>(allActiveConnection.keySet());
        if (CollectionUtil.isNotEmpty(address)) {
            String load = loadHandler.load(address);
            if (StrUtil.isNotBlank(load)) {
                CommunicationClient communicationClient = allActiveConnection.get(load);
                if (communicationClient != null && communicationClient.communicationStatus()) {
                    communicationClient.asyncSendMessage(message);
                    return;
                }
            }
        }
        logger.warning("发送信息失败，被丢弃");
    }

    /**
     * 返回所有活跃的连接
     *
     * @return 所有活跃的连接
     */
    public static Map<String, CommunicationClient> getAllActiveConnection() {
        READ_LOCK.lock();
        try {
            Map<String, CommunicationClient> communicationClientMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(ACTIVE_CONNECTION)) {
                ACTIVE_CONNECTION.forEach(communicationClientMap::put);
            }
            return communicationClientMap;
        } finally {
            READ_LOCK.unlock();
        }
    }

    /**
     * 自动重连
     */
    public static synchronized void reConnectionConfinementConnection() {
        boolean start = IS_START.get();
        if (!start) {
            IS_START.compareAndSet(false, true);

            //探测坏连接缓存，进行重连操作
            scheduledFuture = RE_CONNECTION_POOL.scheduleWithFixedDelay(() -> {
                lock.lock();
                try {
                    if (CollectionUtil.isNotEmpty(FAILURE_CONNECTION)) {
                        logger.warning("存在失效连接， 开始重连任务！");
                        FAILURE_CONNECTION.forEach((key, value) -> value.reConnect());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }, 3, 3, TimeUnit.SECONDS);

            //它存在的意义是防止极端情况发生，因为某些异常原因，导致，连接失效后，没有加入到坏连接缓存中去，导致该连接无法重连
            checkConnection = RE_CONNECTION_POOL.scheduleWithFixedDelay(() -> {
                lock.lock();
                try {
                    List<CommunicationClient> inActiveConnection = new ArrayList<>();
                    Map<String, CommunicationClient> allActiveConnection = getAllActiveConnection();
                    allActiveConnection.forEach((k,v) ->{
                        if(!v.communicationStatus()) {
                            inActiveConnection.add(v);
                        }
                    });
                    if (CollectionUtil.isNotEmpty(inActiveConnection)) {
                        logger.warning("存在失效连接！");
                        inActiveConnection.forEach(ConnectionManager::confinementConnection);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }, 60, 60, TimeUnit.SECONDS);
        }
    }

    public static void setLoadHandler(ThreadXLoadHandler loadHandler) {
        ConnectionManager.loadHandler = loadHandler;
    }
}
