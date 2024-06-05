package com.threadx.metrics.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色信息
 *
 * @author huangfukexing
 * @date 2023/7/24 10:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("role")
public class Role extends BaseEntity{
    private static final long serialVersionUID = -4701790977149672162L;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色详情
     */
    private String roleDesc;
}
