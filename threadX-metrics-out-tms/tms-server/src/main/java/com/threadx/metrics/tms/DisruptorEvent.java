package com.threadx.metrics.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标输出器
 *
 * @author huangfukexing
 * @date 2025/4/28 17:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisruptorEvent<T> {
    private T data;
}