package com.threadx.metrics.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限表
 *
 * @author huangfukexing
 * @date 2023/6/1 14:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("permission")
public class Permission extends BaseEntity{
    private static final long serialVersionUID = -9087343694926446655L;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限表述
     */
    private String permissionKey;

    /**
     * 权限介绍
     */
    private String permissionDesc;
}
