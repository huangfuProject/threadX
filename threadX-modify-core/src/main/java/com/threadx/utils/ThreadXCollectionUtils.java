package com.threadx.utils;

import java.util.*;

/**
 * 集合工具类
 *
 * @author huangfukexing
 * @date 2023/3/10 21:45
 */
public class ThreadXCollectionUtils {

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param iterator Iterator对象
     * @return 是否为空
     */
    public static boolean isEmpty(Iterator<?> iterator) {
        return null == iterator || !iterator.hasNext();
    }

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     * @return 是否为空
     */
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }


    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(Iterable<?> iterable) {
        return null != iterable && isNotEmpty(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param iterator Iterator对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(Iterator<?> iterator) {
        return null != iterator && iterator.hasNext();
    }

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     * @return 是否为空
     */
    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return null != enumeration && enumeration.hasMoreElements();
    }

    /**
     * 遍历集合
     *
     * @param list                需要遍历的集合
     * @param collectionIteration 集合处理器哦
     * @param reversal            是否倒序
     * @param <T>                 元素类型
     */
    public static <T> void iterationList(List<T> list, ThreadXCollectionIteration<T> collectionIteration, boolean reversal) {
        if (reversal) {
            reversalIterationList(list, collectionIteration);
        } else {
            iterationList(list, collectionIteration);
        }
    }

    /**
     * 倒序遍历
     *
     * @param list          需要遍历的额集合
     * @param listIteration 迭代器
     * @param <T>           元素类型
     */
    public static <T> void reversalIterationList(List<T> list, ThreadXCollectionIteration<T> listIteration) {
        for (int i = list.size() - 1; i >= 0; i--) {
            listIteration.iteration(list.get(i));
        }
    }

    /**
     * 正序遍历
     *
     * @param list                需要遍历的集合
     * @param collectionIteration 迭代器
     * @param <T>                 元素类型
     */
    public static <T> void iterationList(List<T> list, ThreadXCollectionIteration<T> collectionIteration) {
        for (T t : list) {
            collectionIteration.iteration(t);
        }
    }

    /**
     * 关联集合
     *
     * @param stackFlow 节点流
     * @param s         分隔符
     * @return 关联后的数据
     */
    public static String join(Collection<String> stackFlow, String s) {
        if (isEmpty(stackFlow)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String node : stackFlow) {
            sb.append(node).append(s);
        }
        String data = sb.toString();
        data = data.substring(0, data.lastIndexOf(s));
        return data;
    }


    public static interface ThreadXCollectionIteration<T> {
        /**
         * 迭代器
         *
         * @param entity 迭代元素
         */
        void iteration(T entity);
    }
}
