package com.threadx.metrics.server.enums;

/**
 * 权限信息
 *
 * @author huangfukexing
 * @date 2023/6/7 14:00
 */
public enum PermissionValue {
    /**
     * 添加用户
     */
    USER_SAVE("save:user","保存用户"),
    /**
     * 查询所有用户信息
     */
    FIND_ALL_USER("findAll:user","查询所有用户信息"),

    /**
     * 查询所有的用户详细信息
     */
    FIND_USER_DESC("find:user","查询所有的用户详细信息"),

    /**
     * 冻结用户
     */
    USER_DISABLE("disable:user","冻结用户"),

    /**
     * 启用用户
     */
    USER_ENABLE("enable:user","启用用户"),

    /**
     * 强制删除用户，以及相关联的所有信息
     */
    FORCE_DELETE_USER("force:delete:user","强制删除用户，以及相关联的所有信息"),

    /**
     * 查询所有的菜单列表
     */
    FIND_ALL_MENU_LIST("findAll:menu","查询所有的菜单列表"),

    /**
     * 查询所有的角色信息
     */
    FIND_ALL_ROLE_LIST("findAll:role","查询所有的角色信息"),

    /**
     * 查询所有的权限信息
     */
    FIND_ALL_PERMISSION_LIST("findAll:permission","查询所有的权限信息"),

    /**
     * 查询角色权限数据
     */
    FIND_ROLE_AUTHORITY("find:role:authority", "查询角色权限数据"),

    /**
     * 保存角色信息
     */
    SAVE_ROLE_AUTHORITY("save:role:authority", "保存角色信息"),

    /**
     * 删除角色信息
     */
    DELETE_ROLE_AUTHORITY("delete:role:authority", "删除角色信息"),

    /**
     * 解绑用户与角色信息
     */
    UNTIE_USER_ROLE("untie:role:authority", "解绑用户与角色信息"),

    /**
     * 查询角色下的用户
     */
    FIND_ROLE_USER("find:role:users", "查询角色下的用户"),


    /**
     * 删除角色信息
     */
    UPDATE_THREAD_POOL_PARAM("update:threadPool:param", "修改线程池参数"),

    /**
     * 查询所有用户的操作日志
     */
    FIND_ALL_LOG("find:all:log", "查询所有用户的操作日志"),

    ;
    /**
     * 权限主键
     */
    private final String permissionKey;
    /**
     * 权限介绍
     */
    private final String permissionDesc;


    PermissionValue(String permissionKey, String permissionDesc) {
        this.permissionKey = permissionKey;
        this.permissionDesc = permissionDesc;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public String getPermissionDesc() {
        return permissionDesc;
    }
}
