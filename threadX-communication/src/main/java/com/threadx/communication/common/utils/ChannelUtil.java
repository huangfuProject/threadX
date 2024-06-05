package com.threadx.communication.common.utils;

import io.netty.channel.Channel;

/**
 * 管道相关的工具类
 *
 * @author huangfu
 * @date 2022年10月12日08:58:44
 */
public class ChannelUtil {

    /**
     * 获取管道的远程地址
     *
     * @param channel 通讯管道
     * @return 远程地址
     */
    public static String getChannelRemoteAddress(Channel channel) {
        return channel.remoteAddress().toString();
    }

    /**
     * 获取管道的local地址
     *
     * @param channel 通讯管道
     * @return 远程地址
     */
    public static String getChannelLocalAddress(Channel channel) {
        return channel.localAddress().toString();
    }

    /**
     * 获取管道的长id
     *
     * @param channel 管道
     * @return 长id
     */
    public static String getChannelLongId(Channel channel) {
        return channel.id().asLongText();
    }

    /**
     * 获取管道的短id
     *
     * @param channel 管道
     * @return 短id
     */
    public static String getChannelShortId(Channel channel) {
        return channel.id().asShortText();
    }
}
