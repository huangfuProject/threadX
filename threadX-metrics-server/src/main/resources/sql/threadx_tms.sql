/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.3.206
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : 192.168.3.206:3306
 Source Schema         : threadx_tms

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 11/06/2023 00:08:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS threadx_tms;
CREATE DATABASE threadx_tms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE threadx_tms;

-- ----------------------------
-- Table structure for active_log
-- ----------------------------
DROP TABLE IF EXISTS `active_log`;
CREATE TABLE `active_log` (
                              `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `active_key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动key标记',
                              `active_log` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行为日志',
                              `start_time` bigint(64) NOT NULL COMMENT '活动时间',
                              `end_time` bigint(64) NOT NULL COMMENT '结束时间',
                              `user_id` bigint(64) DEFAULT NULL COMMENT '活跃用户',
                              `browser` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '浏览器信息',
                              `os` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统信息',
                              `ip_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '访问的ip地址',
                              `operation_time` bigint(64) NOT NULL COMMENT '操作耗时',
                              `result_state` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '0 成功  1 失败',
                              `error_message` longtext COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
                              `update_time` bigint(64) NOT NULL COMMENT '修改时间',
                              `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                              `param_data` longtext COLLATE utf8mb4_unicode_ci COMMENT '调用参数',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for instance_item
-- ----------------------------
DROP TABLE IF EXISTS `instance_item`;
CREATE TABLE `instance_item` (
                                 `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `instance_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '实例名称',
                                 `server_id` bigint(32) NOT NULL COMMENT '所属服务的名称',
                                 `server_name` varchar(255) NOT NULL COMMENT '服务名称',
                                 `active_time` bigint(64) NOT NULL COMMENT '活跃时间',
                                 `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                                 `update_time` bigint(64) DEFAULT NULL COMMENT '修改时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `server_id_index` (`server_id`) USING BTREE COMMENT '服务id创建索引'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实例名称';


-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
                        `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `menu_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
                        `menu_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单对应的',
                        `menu_icon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对应的图标',
                        `menu_desc` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单介绍',
                        `sort` int(12) NOT NULL DEFAULT '0' COMMENT '排序字段',
                        `state` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '菜单状态   0  正常   禁用',
                        `update_time` bigint(64) NOT NULL COMMENT '修改时间',
                        `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户菜单';

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` (`id`, `menu_name`, `menu_url`, `menu_icon`, `menu_desc`, `sort`, `state`, `update_time`, `create_time`) VALUES (1, '工作台', '/worktable', 'icon-gongzuotai', '工作台信息', 0, '0', 1685605885371, 1685605885371);
INSERT INTO `menu` (`id`, `menu_name`, `menu_url`, `menu_icon`, `menu_desc`, `sort`, `state`, `update_time`, `create_time`) VALUES (2, '项目管理', '/servicePage', 'icon-icon-project', '项目信息', 10, '0', 1685605885371, 1685605885371);
INSERT INTO `menu` (`id`, `menu_name`, `menu_url`, `menu_icon`, `menu_desc`, `sort`, `state`, `update_time`, `create_time`) VALUES (3, '角色管理', '/roleManager', 'icon-jiaose', '角色管理', 20, '0', 1690171318171, 1690171318171);
INSERT INTO `menu` (`id`, `menu_name`, `menu_url`, `menu_icon`, `menu_desc`, `sort`, `state`, `update_time`, `create_time`) VALUES (4, '用户管理', '/userManager', 'icon-yonghuguanli', '用户管理', 30, '0', 1686205203298, 1686205203298);
COMMIT;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
                              `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `permission_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
                              `permission_key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限key',
                              `permission_desc` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单介绍',
                              `update_time` bigint(64) NOT NULL COMMENT '修改时间',
                              `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户权限';

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (1, '添加用户', 'save:user', '管理员保存用户的权限信息', 1685770093193, 1685770093193);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (2, '查询所有用户信息', 'findAll:user', '管理员查询所有的用户信息的权限', 1689665860546, 1689665860546);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (3, '禁用用户', 'disable:user', '管理员禁用用户', 1689735117790, 1689735117790);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (4, '启用用户', 'enable:user', '管理员启用用户', 1689735146792, 1689735146792);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (5, '强制删除用户', 'force:delete:user', '强制删除用户，以及相关联的所有信息', 1689832529179, 1689832529179);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (6, '查询菜单列表', 'findAll:menu', '查询所有的菜单列表', 1689935409302, 1689935409302);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (7, '查询所有的角色信息', 'findAll:role', '查询所有的角色信息', 1690182972277, 1690182972277);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (8, '查询所有的权限信息', 'findAll:permission', '查询所有的权限信息', 1690251235159, 1690251235159);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (9, '查询角色权限数据', 'find:role:authority', '查询角色的具体权限信息', 1690265952461, 1690265952461);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (10, '保存角色信息', 'save:role:authority', '保存角色信息', 1690278500997, 1690278500997);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (11, '删除角色信息', 'delete:role:authority', '删除角色信息', 1690352890731, 1690352890731);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (12, '解绑用户角色', 'untie:role:authority', '解除用户对角色的绑定', 1690356827122, 1690356827122);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (13, '查询角色关联用户', 'find:role:users', '查询当前角色下绑定的用户', 1690359317749, 1690359317749);
INSERT INTO `permission` (`id`, `permission_name`, `permission_key`, `permission_desc`, `update_time`, `create_time`) VALUES (14, '查询用户详情', 'find:user', '查询用户的详细信息', 1690541127433, 1690541127433);
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `role_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
                        `role_desc` longtext COLLATE utf8mb4_unicode_ci COMMENT '角色解释',
                        `update_time` bigint(64) NOT NULL COMMENT '修改时间',
                        `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `name_index` (`role_name`) USING HASH COMMENT '唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `role_name`, `role_desc`, `update_time`, `create_time`) VALUES (1, '超级管理员', '拥有所有的权限', 1690164464238, 1690164464238);
INSERT INTO `role` (`id`, `role_name`, `role_desc`, `update_time`, `create_time`) VALUES (4, '开发者', '开发者', 1690278804065, 1690278804065);
COMMIT;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
                             `role_id` bigint(64) NOT NULL COMMENT '角色id',
                             `menu_id` bigint(64) NOT NULL COMMENT '菜单id',
                             PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES (1, 1);
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES (1, 2);
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES (1, 3);
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES (1, 4);
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES (4, 1);
COMMIT;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
                                   `role_id` bigint(64) NOT NULL COMMENT '角色id',
                                   `permission_id` bigint(64) NOT NULL COMMENT '权限id',
                                   PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 1);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 2);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 3);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 4);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 5);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 6);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 7);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 8);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 9);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 10);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 11);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 12);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 13);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 14);
COMMIT;

-- ----------------------------
-- Table structure for server_item
-- ----------------------------
DROP TABLE IF EXISTS `server_item`;
CREATE TABLE `server_item` (
                               `id` bigint(32) NOT NULL AUTO_INCREMENT,
                               `server_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务名称',
                               `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                               `update_time` bigint(64) DEFAULT NULL COMMENT '修改时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='记录当前监控的服务';


-- ----------------------------
-- Table structure for thread_pool_data
-- ----------------------------
DROP TABLE IF EXISTS `thread_pool_data`;
CREATE TABLE `thread_pool_data` (
                                    `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `server_key` varchar(255) NOT NULL COMMENT '服务名称',
                                    `instance_key` varchar(255) NOT NULL COMMENT '实例名称',
                                    `address` varchar(255) NOT NULL COMMENT '地址',
                                    `thread_pool_object_id` varchar(255) NOT NULL COMMENT '线程池对象的id',
                                    `thread_pool_group_name` varchar(255) NOT NULL COMMENT '线程池组的名称',
                                    `thread_pool_name` varchar(255) NOT NULL COMMENT '线程池的名称',
                                    `core_pool_size` int(32) NOT NULL COMMENT '线程池的核心数量',
                                    `maximum_pool_size` int(32) NOT NULL COMMENT '最大可执行任务的线程数',
                                    `active_count` int(32) NOT NULL COMMENT '当前活跃的线程数',
                                    `this_thread_count` int(32) NOT NULL COMMENT '当前线程池的线程数量  包含没有执行任务的线程还没有来得及被销毁的非核心线程',
                                    `largest_pool_size` int(32) NOT NULL COMMENT '曾将达到的最大的线程数 历史信息',
                                    `rejected_count` bigint(64) NOT NULL COMMENT '拒绝执行的次数',
                                    `task_count` bigint(64) NOT NULL COMMENT '堆积的、执行中的、已经完成的任务的总和',
                                    `completed_task_count` bigint(64) NOT NULL COMMENT '已经完成的数量',
                                    `queue_type` varchar(255) DEFAULT NULL COMMENT '队列类型',
                                    `rejected_type` varchar(255) DEFAULT NULL COMMENT '拒绝策略',
                                    `thread_pool_flow` longtext COMMENT '线程池的创建流程（堆栈转化）',
                                    `keep_alive_time` bigint(64) DEFAULT NULL COMMENT '毫秒 线程空闲',
                                    `instance_id` bigint(32) NOT NULL COMMENT '所属实例的id',
                                    `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                                    `update_time` bigint(64) DEFAULT NULL COMMENT '修改时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `object_id_index` (`thread_pool_object_id`) USING BTREE COMMENT '线程池对象id索引',
                                    KEY `instance_id_index` (`instance_id`) USING BTREE COMMENT '对实例id查询建立索引'
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='线程池的监控数据';


-- ----------------------------
-- Table structure for thread_task_data
-- ----------------------------
DROP TABLE IF EXISTS `thread_task_data`;
CREATE TABLE `thread_task_data` (
                                    `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `server_key` varchar(255) NOT NULL COMMENT '服务名称',
                                    `instance_key` varchar(255) NOT NULL COMMENT '实例名称',
                                    `thread_pool_object_id` varchar(255) NOT NULL COMMENT '线程池的对象id',
                                    `address` varchar(255) NOT NULL COMMENT '地址',
                                    `thread_pool_group_name` varchar(255) NOT NULL COMMENT '线程池组的名称',
                                    `thread_pool_name` varchar(255) NOT NULL COMMENT '线程池的名称',
                                    `thread_name` varchar(255) DEFAULT NULL COMMENT '被那一个线程执行的',
                                    `submit_time` bigint(64) DEFAULT NULL COMMENT '该时间为任务被提交的时间，只要该任务被加载进线程池，这个时间就会被初始化',
                                    `start_time` bigint(64) DEFAULT NULL COMMENT '任务开始运行的时间，注意，这里的开始时间是任务真正开始运行的时间，不是提交的时间，因为他可能被堆积在队列中',
                                    `end_time` bigint(64) DEFAULT NULL COMMENT '任务的结束时间，无奈他是正常结束，或者是异常，这个时间都会存在，当然，被拒绝的任务不在此列！',
                                    `runIng_consuming_time` bigint(64) DEFAULT NULL COMMENT '任务的执行耗时',
                                    `wait_time` bigint(64) DEFAULT NULL COMMENT '任务等待时间',
                                    `consuming_time` bigint(64) DEFAULT NULL COMMENT '任务总耗时',
                                    `refuse` char(1) DEFAULT NULL COMMENT '0  false  1 true   当任务被拒绝策略执行的时候，该值为true,否则为false!',
                                    `success` char(1) DEFAULT NULL COMMENT '0  false  1 true  任务是否被执行成功，如果中途异常、被拒绝，该值都会被设置为false, 否则为true',
                                    `throwable` longtext COMMENT '任务的异常信息，当没有异常的时候，这个值为空！',
                                    `instance_id` bigint(32) NOT NULL COMMENT '所属实例的id',
                                    `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                                    `update_time` bigint(64) DEFAULT NULL COMMENT '修改时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `instance_id_thread_pool_group_name_thread_pool_nam_success_index` (`instance_id`,`thread_pool_group_name`,`thread_pool_name`,`success`) USING BTREE,
                                    KEY `objectId_index` (`thread_pool_object_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=632 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='线程池内任务执行信息';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `user_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                        `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密码',
                        `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
                        `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户邮箱',
                        `state` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0  冻结  1 正常',
                        `create_time` bigint(64) NOT NULL COMMENT '创建时间',
                        `update_time` bigint(64) DEFAULT NULL COMMENT '修改时间',
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `user_name_index` (`user_name`) USING BTREE COMMENT '用户用户名 唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='threadx的用户信息';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `user_name`, `password`, `nick_name`, `email`, `state`, `create_time`, `update_time`) VALUES (1, 'admin', '$2a$10$BQrmrFvsElKyr/b4j8wkVuxsYAuIdaWzs8UbR2PsTmR9ewveAyKpG', '管理员', 'admin@163.com', '1', 1685584117047, 1685584117047);
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
                             `user_id` bigint(64) NOT NULL COMMENT '用户id',
                             `role_id` bigint(64) NOT NULL COMMENT '角色id',
                             PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (2, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
