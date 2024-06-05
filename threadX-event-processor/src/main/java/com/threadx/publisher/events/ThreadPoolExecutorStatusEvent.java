package com.threadx.publisher.events;

import com.threadx.metrics.ThreadPoolExecutorData;
import com.threadx.publisher.ThreadXStatusEvent;
import lombok.*;

import java.io.Serializable;

/**
 * 线程池指标事件源
 *
 * @author huangfukexing
 * @date 2023/3/17 14:33
 */

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThreadPoolExecutorStatusEvent extends ThreadPoolExecutorData implements ThreadXStatusEvent, Serializable {

    private static final long serialVersionUID = 7953959914662961813L;

}
