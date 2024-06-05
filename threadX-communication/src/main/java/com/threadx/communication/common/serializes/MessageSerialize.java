package com.threadx.communication.common.serializes;

/**
 * 对象序列化、反序列化接口
 *
 * @author huangfu
 * @date 2022年10月10日08:33:35
 */
public interface MessageSerialize<T> {
    /**
     * 对象序列化
     * @param object 要序列化的对象
     * @return 序列化成字节的对象
     */
    byte[] serializeObject(Object object);

    /**
     * 反序列化对象
     * @param data 要反序列化成对象的字节
     * @param clazz 要反序列化成的类型
     * @return 返回序列化好的对象
     */
    T objectDeserialize(byte[] data, Class<T> clazz);
}
