package com.threadx.communication.common.serializes;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Protostuff序列化对象
 *
 * @author huangfu
 * @date 2022年10月10日08:36:36
 */
public class ProtostuffSerialize<T> implements MessageSerialize<T> {

    /**
     * 编解码对象载体对象
     * 这里是为了确认一个类型  方便编解码
     */
    private static class SerializeData {
        private Object target;
    }

    @Override
    public byte[] serializeObject(Object object) {
        //创建包装体
        SerializeData serializeData = new SerializeData();
        //包装对象
        serializeData.target = object;
        //获取要序列化的类型
        Class<SerializeData> serializeDataClass = (Class<SerializeData>) serializeData.getClass();
        //缓冲流
        LinkedBuffer linkedBuffer = LinkedBuffer.allocate(1024 * 4);
        try {
            //获取序列化模式  真正吧对象写进缓冲器的存在
            Schema<SerializeData> schema = getSerializeDataSchema(serializeDataClass);
            //开始序列化
            return ProtostuffIOUtil.toByteArray(serializeData, schema, linkedBuffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            linkedBuffer.clear();
        }
    }

    @Override
    public T objectDeserialize(byte[] data, Class<T> clazz) {
        try {
            //获取序列化模式  注意需要和和序列化的时候保持一致
            Schema<SerializeData> schema = getSerializeDataSchema(SerializeData.class);
            //创建一个消息对象
            SerializeData serializeData = schema.newMessage();
            //合并消息对象
            ProtostuffIOUtil.mergeFrom(data, serializeData, schema);
            //返回序列化后的对象
            return (T) serializeData.target;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 获取序列化模式
     * 这里拆出来的原因是因为 序列化和反序列化必须使用相同的模式  为了保持统一 所以拆出来
     *
     * @param serializeDataClass 需要序列化的类型
     * @return 返回序列化模式
     */
    private Schema<SerializeData> getSerializeDataSchema(Class<SerializeData> serializeDataClass) {
        return RuntimeSchema.getSchema(serializeDataClass);
    }
}
