package com.threadx.metrics.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 线程任务错误统计<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/10 20:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadTaskDataErrorCalculation implements Serializable {
    private static final long serialVersionUID = -5817507476193865286L;

    /**
     * 实例的信息
     */
    private Long instanceId;

    /**
     * 线程池组的名称
     */
    private String threadPoolGroupName;

    /**
     * 错误数量
     */
    private Long errorCount;
}
