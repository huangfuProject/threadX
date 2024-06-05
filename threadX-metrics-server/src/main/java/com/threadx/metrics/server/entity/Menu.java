package com.threadx.metrics.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单映射
 *
 * @author huangfukexing
 * @date 2023/6/1 14:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("menu")
public class Menu extends BaseEntity {
    private static final long serialVersionUID = 2634739896324634586L;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单的url
     */
    private String menuUrl;

    /**
     * 菜单的图标
     */
    private String menuIcon;

    /**
     * 菜单介绍
     */
    private String menuDesc;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 菜单状态   0  正常   禁用
     */
    private String state;

}
