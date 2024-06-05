package com.threadx.publisher.events;

import com.threadx.metrics.ThreadTaskExecutorData;
import com.threadx.publisher.ThreadXStatusEvent;
import com.threadx.utils.ThreadXThrowableMessageUtil;
import lombok.*;

import java.io.Serializable;

/**
 * 线程的任务状态，每一个对象唯一绑定一个任务对象，该任务对象生命周期与任务的生命周期相同！
 *
 * @author huangfukexing
 * @date 2023/3/9 18:00
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ThreadPoolExecutorThreadTaskState extends ThreadTaskExecutorData implements ThreadXStatusEvent, Serializable {
    private static final long serialVersionUID = 7974501570560431710L;
}
