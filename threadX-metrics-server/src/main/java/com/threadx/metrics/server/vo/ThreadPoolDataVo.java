package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * *************************************************<br/>
 * 线程池的数据VO<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/5 10:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "线程池数据的映射对象", value = "线程池数据的映射对象")
public class ThreadPoolDataVo implements Serializable {
    private static final long serialVersionUID = -8637536381715762198L;

    public final static String ACTION_NAME = "正在执行任务";
    public final static String IDEA_NAME = "等待任务提交";
    public final static String DISCONNECTION = "断连";
    /**
     * 线程池的id
     */
    @ApiModelProperty(name = "id", value = "线程池的id")
    private Long id;

    /**
     * 线程池的id
     */
    @ApiModelProperty(name = "instanceId", value = "所属实例的id")
    private Long instanceId;
    /**
     * 线程池的名称
     */
    @ApiModelProperty(name = "threadPoolName", value = "线程池的名称")
    private String threadPoolName;

    /**
     * 线程池的组的名称
     */
    @ApiModelProperty(name = "threadPoolGroupName", value = "线程池的组的名称")
    private String threadPoolGroupName;

    /**
     * 当前活跃的线程数
     */
    @ApiModelProperty(name = "activeCount", value = "当前活跃的线程数，只计算正在执行任务的线程！")
    private Integer activeCount;

    /**
     * 当前线程池的线程数量  包含没有执行任务的线程还没有来得及被销毁的非核心线程
     */
    @ApiModelProperty(name = "thisThreadCount", value = "当前线程池的线程数量  包含没有执行任务的线程还没有来得及被销毁的非核心线程，不管线程是否空闲都会计算")
    private Integer thisThreadCount;


    /**
     * 已经完成的数量
     */
    @ApiModelProperty(name = "completedTaskCount", value = "已经完成的数量")
    private Long completedTaskCount;


    /**
     * 数据创建时间
     */
    @ApiModelProperty(name = "createTime", value = "数据创建时间")
    private String createTime;


    /**
     * 数据更新
     */
    @ApiModelProperty(name = "updateTime", value = "数据更新")
    private String updateTime;


    /**
     * 当前线程池的状态
     */
    @ApiModelProperty(name = "state", value = "当前线程池的状态")
    private String state;

    /**
     * 当前线程池的对象的id
     */
    @ApiModelProperty(name = "objectId", value = "当前线程池的对象的id")
    private String objectId;

    /**
     * 线程池的创建流程
     */
    @ApiModelProperty(name = "createThreadPoolFlow", value = "线程池创建流程(顺序)")
    private List<ProcedureVo> createThreadPoolFlow;

}
