/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50626
 Source Host           : localhost
 Source Database       : deploy

 Target Server Type    : MySQL
 Target Server Version : 50626
 File Encoding         : utf-8

 Date: 03/27/2018 15:24:07 PM
*/

CREATE DATABASE IF NOT EXISTS deploy DEFAULT CHARACTER SET = utf8mb4;

Use deploy;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_account`
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `uid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `account_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-普通帐号，1-ldap帐号',
  `account` varchar(50) NOT NULL COMMENT '帐号ID',
  `operator` int(11) NOT NULL DEFAULT '0' COMMENT '操作人id',
  `realname` varchar(50) NOT NULL COMMENT '真名',
  `mobile_no` varchar(20) NOT NULL DEFAULT '' COMMENT '电话号码',
  `last_modify` datetime NOT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `account_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户状态，1-正常,2-冻结',
  `default_data` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否是基本用户( 可以理解为是否是管理员)，0-不是，1-是,不可以删除',
   UNIQUE KEY `uniq_key_account_accountType` (`account`,`account_type`),
   PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
--  Table structure for `t_account_role_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_account_role_relation`;
CREATE TABLE `t_account_role_relation` (
  `uid` bigint(11) NOT NULL COMMENT ' 用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`uid`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
--  Table structure for `t_admin_operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_operation_log`;
CREATE TABLE `t_admin_operation_log` (
  `log_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) NOT NULL COMMENT '管理员ID',
  `operation_type` varchar(30) NOT NULL COMMENT '操作类型',
  `arguments` varchar(512) NOT NULL DEFAULT '' COMMENT '参数',
  `result` varchar(255) NOT NULL DEFAULT '' COMMENT '执行结果',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `ext1` varchar(64) NOT NULL DEFAULT '' COMMENT '扩展字段1',
  `ext2` varchar(64) NOT NULL DEFAULT '' COMMENT '扩展字段2',
  PRIMARY KEY (`log_id`),
  KEY `idx_account_time` (`uid`,`create_time`),
  KEY `idx_time_operation_type` (`create_time`,`operation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员操作日志';

-- ----------------------------
--  Table structure for `t_app_define`
-- ----------------------------
DROP TABLE IF EXISTS `t_app_define`;
CREATE TABLE `t_app_define` (
  `app_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `app_name` varchar(30) NOT NULL COMMENT '名称',
  `display_name` varchar(30) NOT NULL COMMENT '用于显示的名称',
  `app_key` varchar(80) NOT NULL COMMENT '系统连接的密钥',
  `summary` varchar(255) NOT NULL COMMENT '系统描述',
  `app_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可用,1可用，2不可用',
  `login_url` varchar(200) NOT NULL COMMENT '切换系统的登录url',
  `sequence` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `operator` int(11) NOT NULL COMMENT '操作者',
  `last_modify` datetime NOT NULL COMMENT '更新时间',
  `bug_report_url` varchar(200) NOT NULL DEFAULT '#' COMMENT '报bug url',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `uniq_Key_app_name` (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用系统信息';

-- ----------------------------
--  Table structure for `t_deploy_history`
-- ----------------------------
DROP TABLE IF EXISTS `t_deploy_history`;
CREATE TABLE `t_deploy_history` (
  `history_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '版本发布历史ID',
  `title` varchar(80) NOT NULL COMMENT '发布版本的标题',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `account_id` int(11) NOT NULL COMMENT '发布者id',
  `deploy_time` datetime DEFAULT NULL COMMENT '发布时间',
  `result` tinyint(4) NOT NULL COMMENT '1 是全部成功 2是部分成功 3是全部失败',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `auditor_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '审核人ID',
  `tag_name` varchar(60) NOT NULL DEFAULT '' COMMENT '发布的tag/分支，相对地址，例如 20161025，和 t_project_config.repo_url组合起来就是完整地址。如果发布trunk，这里可以为""',
  `version_no` varchar(30) NOT NULL DEFAULT '' COMMENT '发布的版本号',
  `is_restart` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是重启，0不是，1是',
  `is_rollback` tinyint(4) NOT NULL DEFAULT '0' COMMENT '本次发布是否是回滚操作, 0不是  1是',
  `rollback_to_deploy_id` int(11) NOT NULL DEFAULT '0' COMMENT '回滚到的版本发布历史ID',
  `module_id` int(11) NOT NULL COMMENT '哪个模块部署的',
  `module_name` varchar(80) NOT NULL COMMENT '模块名称',
  `env_id` int(11) NOT NULL COMMENT '环境',
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `deploy_servers` int(11) NOT NULL COMMENT '发布的服务器数量',
  `success_count` int(11) NOT NULL COMMENT '发布成功的服务器数量',
  `concurrent_server_percentage` tinyint(4) NOT NULL DEFAULT '0' COMMENT '并发发布服务器百分比',
  `deploy_time_interval` smallint(11) NOT NULL COMMENT '发布时间间隔,单位是秒  ',
  `deploy_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发布状态： 1代表等待审核，2代表等待部署，3代表审核拒绝，4代表发布取消，5已经发布',
  `force_compile` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否强制编译，0不 1强制编译',
  `real_name` varchar(50) NOT NULL COMMENT '发布者真实姓名',
  `server_strategy` tinyint(4) NOT NULL DEFAULT '1' COMMENT '服务器发布策略，1 是遇到失败马上停止，2是无论有没失败，都继续',
  PRIMARY KEY (`history_id`),
  KEY `idx_project_id` (`project_id`,`create_time`),
  KEY `idx_module_id` (`module_id`,`create_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deployTime` (`deploy_time`) USING BTREE,
  KEY `idx_account_id` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布历史';

-- ----------------------------
--  Table structure for `t_global_setting`
-- ----------------------------
DROP TABLE IF EXISTS `t_global_setting`;
CREATE TABLE `t_global_setting` (
  `setting_id` int(11) NOT NULL COMMENT '主键',
  `svn_checkout_dir` varchar(200) NOT NULL COMMENT 'svn checkout 目录',
  `target_server_user` varchar(80) NOT NULL COMMENT '目标服务器用户',
  `target_server_dir` varchar(80) NOT NULL COMMENT '目标服务器目录',
  `idc` varchar(80) NOT NULL DEFAULT '' COMMENT 'idc ',
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局配置表';

-- ----------------------------
--  Table structure for `t_low_quality_rank`
-- ----------------------------
DROP TABLE IF EXISTS `t_low_quality_rank`;
CREATE TABLE `t_low_quality_rank` (
  `rank_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `module_id` int(11) NOT NULL COMMENT '模块ID',
  `deploy_times` int(11) NOT NULL COMMENT '成功发布次数，失败的不计算在内',
  PRIMARY KEY (`rank_id`),
  UNIQUE KEY `uniq_key_stat_date_module` (`stat_date`,`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='最差发布质量排行，只统计生产环境';

-- ----------------------------
--  Table structure for `t_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menu_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_menu_id` int(11) NOT NULL DEFAULT '0' COMMENT '父菜单id',
  `app_id` int(11) NOT NULL COMMENT '应用系统id',
  `menu_name` varchar(30) NOT NULL COMMENT '名称',
  `url` varchar(200) NOT NULL COMMENT '资源',
  `sequence` int(11) NOT NULL DEFAULT '0' COMMENT '排序id号',
  `operator` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作者',
  `last_modify` datetime NOT NULL COMMENT '更新时间',
  `hide` tinyint(1) NOT NULL DEFAULT '0' COMMENT '菜单是否隐藏，true隐藏,false不隐藏',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `uniq_Key_app_id` (`app_id`,`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2133326262 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- ----------------------------
--  Table structure for `t_menu_resources_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_resources_relation`;
CREATE TABLE `t_menu_resources_relation` (
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `res_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`menu_id`,`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单与资源对应表';

-- ----------------------------
--  Records of `t_menu_resources_relation`
-- ----------------------------
--  Table structure for `t_module_conf`
-- ----------------------------
DROP TABLE IF EXISTS `t_module_conf`;
CREATE TABLE `t_module_conf` (
  `module_id` int(11) NOT NULL COMMENT '模块ID',
  `conf_type` int(11) NOT NULL COMMENT '配置类型,1-resin, 2-tomcat, 3-其他',
  `conf_value` varchar(512) NOT NULL COMMENT '配置，json格式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`module_id`,`conf_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块配置表';

-- ----------------------------
--  Table structure for `t_module_jvm`
-- ----------------------------
DROP TABLE IF EXISTS `t_module_jvm`;
CREATE TABLE `t_module_jvm` (
  `module_jvm_id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_id` int(20) NOT NULL COMMENT '模块id',
  `env_id` int(20) NOT NULL COMMENT '环境id',
  `env_name` varchar(50) NOT NULL COMMENT '环境名称',
  `jvm_args` varchar(255) NOT NULL DEFAULT '' COMMENT 'jvm 参数',
  PRIMARY KEY (`module_jvm_id`),
  KEY `idx_moduleId` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块在每个环境的jvm参数';

-- ----------------------------
--  Table structure for `t_project`
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `project_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_name` varchar(60) NOT NULL COMMENT '项目名称，全局唯一，不能重名',
  `structure_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '工程结构类型，1空模块工程 0多模块工程',
  `manager_id` int(11) NOT NULL COMMENT '项目管理员ID',
  `manager_name` varchar(40) NOT NULL DEFAULT '' COMMENT '管理员名称',
  `manager_email` varchar(60) NOT NULL DEFAULT '' COMMENT '管理员email',
  `manager_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '管理员电话',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `project_no` varchar(50) NOT NULL COMMENT '项目编号',
  `program_language` varchar(20) NOT NULL COMMENT '项目的后台编程语言',
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `uniq_projectName` (`project_name`) USING BTREE,
  UNIQUE KEY `uniq_projectNo` (`project_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目';

-- ----------------------------
--  Table structure for `t_project_account_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_project_account_relation`;
CREATE TABLE `t_project_account_relation` (
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `account_id` bigint(11) NOT NULL COMMENT '用户ID',
  `is_admin` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否为管理员，0不是，1是 ，一个项目至少有一个管理员',
  PRIMARY KEY (`project_id`,`account_id`),
  KEY `idx_account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目和成员的关系表';

-- ----------------------------
--  Table structure for `t_project_env`
-- ----------------------------
DROP TABLE IF EXISTS `t_project_env`;
CREATE TABLE `t_project_env` (
  `env_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `env_name` varchar(40) NOT NULL COMMENT '环境名称，dev test pre online',
  `online_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是线上环境，0不是，1 是',
  PRIMARY KEY (`env_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='环境配置表';

-- ----------------------------
--  Table structure for `t_project_module`
-- ----------------------------
DROP TABLE IF EXISTS `t_project_module`;
CREATE TABLE `t_project_module` (
  `module_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_name_zh` varchar(40) NOT NULL DEFAULT '' COMMENT '模块中文名称',
  `module_name` varchar(80) NOT NULL DEFAULT '' COMMENT '模块名称',
  `module_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '模块类型，1代表web项目 2代表dubbo服务',
  `src_path` varchar(200) NOT NULL DEFAULT '' COMMENT 'SVN上的目录，比如 service-impl/target/*.jar'
  `deploy_args` varchar(1024) NOT NULL DEFAULT '' COMMENT '部署自定变量',
  `pre_deploy` varchar(500) NOT NULL DEFAULT '' COMMENT '部署前执行的shell',
  `post_deploy` varchar(500) NOT NULL DEFAULT '' COMMENT '部署后执行的shell',
  `pre_shell` varchar(500) NOT NULL DEFAULT '' COMMENT '发布前执行的shell',
  `post_shell` varchar(500) NOT NULL DEFAULT '' COMMENT '发布后执行的shell',
  `log_name` varchar(200) NOT NULL DEFAULT '' COMMENT '日志名称，读取日志并返回',
  `repo_url` varchar(200) NOT NULL DEFAULT '' COMMENT '版本管理地址，比如：svn://a.b.com/project/tags',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `compile_shell` varchar(200) NOT NULL COMMENT '编译脚本',
  `stop_shell` varchar(200) NOT NULL DEFAULT '' COMMENT '停止服务脚本',
  `restart_shell` varchar(200) NOT NULL COMMENT '重启服务脚本',
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `need_audit` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否需要审核，0不需要，1需要',
  `repo_auth_id` int(11) NOT NULL COMMENT '仓库认证ID',
  `jvm_args` varchar(255) NOT NULL DEFAULT '' COMMENT 'JVM参数表',
  PRIMARY KEY (`module_id`),
  KEY `idx_moduleName` (`module_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块详情';

-- ----------------------------
--  Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(40) NOT NULL COMMENT '角色名称',
  `role_type` int(11) NOT NULL DEFAULT '0' COMMENT '角色类型',
  `remarks` varchar(200) NOT NULL COMMENT '备注',
  `operator` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作者',
  `last_modify` datetime NOT NULL COMMENT '操作时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色定义';

-- ----------------------------
--  Table structure for `t_role_app_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_app_relation`;
CREATE TABLE `t_role_app_relation` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `app_id` int(11) NOT NULL COMMENT '应用系统id',
  PRIMARY KEY (`role_id`,`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录角色对应用系统的访问关系';

-- ----------------------------
--  Table structure for `t_role_menu_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu_relation`;
CREATE TABLE `t_role_menu_relation` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
--  Table structure for `t_role_res_relation`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_res_relation`;
CREATE TABLE `t_role_res_relation` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `res_id` int(11) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`role_id`,`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与资源对应关系';

-- ----------------------------
--  Table structure for `t_server`
-- ----------------------------
DROP TABLE IF EXISTS `t_server`;
CREATE TABLE `t_server` (
  `server_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `server_name` varchar(100) NOT NULL COMMENT '主键',
  `ip` varchar(40) NOT NULL COMMENT 'ip地址',
  `ip_shell_args` varchar(1024) NOT NULL DEFAULT '' COMMENT '当前进程特定shell启动参数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `group_id` int(11) NOT NULL COMMENT '所在的服务器组',
  PRIMARY KEY (`server_id`),
  KEY `idx_groupId` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器表';

-- ----------------------------
--  Table structure for `t_server_deploy_history`
-- ----------------------------
DROP TABLE IF EXISTS `t_server_deploy_history`;
CREATE TABLE `t_server_deploy_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `history_id` int(11) NOT NULL COMMENT '发布历史id',
  `server_id` int(11) NOT NULL COMMENT '服务器id',
  `server_name` varchar(100) NOT NULL COMMENT '服务器名称',
  `server_ip` varchar(40) NOT NULL COMMENT '服务器IP',
  `startup_time` datetime DEFAULT NULL COMMENT '该服务器发布时间',
  `deploy_status` tinyint(4) NOT NULL COMMENT ' 1-发布成功，2-发布失败 3-等待部署',
  PRIMARY KEY (`id`),
  KEY `idx_history_id` (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布历史';

-- ----------------------------
--  Table structure for `t_server_deploy_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_server_deploy_log`;
CREATE TABLE `t_server_deploy_log` (
  `log_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `server_deploy_id` int(11) NOT NULL COMMENT '服务器部署记录的id，对应的是t_server_deploy_history的主键',
  `shell_log` varchar(1000) NOT NULL DEFAULT '' COMMENT 'shell 日志',
  `create_time` datetime NOT NULL COMMENT '插入时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_server_deploy_id_uindex` (`server_deploy_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器发布shell日志记录';

-- ----------------------------
--  Table structure for `t_server_group`
-- ----------------------------
DROP TABLE IF EXISTS `t_server_group`;
CREATE TABLE `t_server_group` (
  `group_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `group_name` varchar(50) NOT NULL DEFAULT '' COMMENT '组名',
  `env_id` int(11) NOT NULL COMMENT '所属环境',
  `module_id` int(11) NOT NULL COMMENT '关联的模块',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器组';

-- ----------------------------
--  Table structure for `t_stat_all`
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_all`;
CREATE TABLE `t_stat_all` (
  `stat_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `env_id` int(11) NOT NULL COMMENT '环境ID',
  `deploy_times` int(11) NOT NULL COMMENT '发布次数=success+failure',
  `success` int(11) NOT NULL COMMENT '成功次数',
  `failure` int(11) NOT NULL COMMENT '失败次数',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uniq_key_stat_date_env` (`stat_date`,`env_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='总的发布次数，根据deploy_time统计，未开始发布和result/stop的不算';

-- ----------------------------
--  Table structure for `t_stat_project`
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_project`;
CREATE TABLE `t_stat_project` (
  `stat_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `env_id` int(11) NOT NULL COMMENT '环境ID',
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `deploy_times` int(11) NOT NULL COMMENT '发布次数=success+failure',
  `success` int(11) NOT NULL COMMENT '成功次数',
  `failure` int(11) NOT NULL COMMENT '失败次数',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uniq_key_stat_date_env_project` (`stat_date`,`env_id`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目的发布次数统计，根据deploy_time统计，未开始发布和result/stop的不算';

-- ----------------------------
--  Table structure for `t_url_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_url_resource`;
CREATE TABLE `t_url_resource` (
  `res_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `parent_res_id` int(11) NOT NULL DEFAULT '0' COMMENT '父资源ID',
  `url_name` varchar(30) NOT NULL COMMENT '名称',
  `uri` varchar(200) NOT NULL COMMENT '资源',
  `operator` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作者',
  `last_modify` datetime NOT NULL COMMENT '更新时间',
  `sequence` int(11) NOT NULL DEFAULT '0' COMMENT '排序id号',
  `app_id` int(11) NOT NULL COMMENT '应用系统id',
  `url_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '资源类型，如1 url,2 button 等',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`res_id`),
  UNIQUE KEY `uniq_Key_app_uri` (`app_id`,`uri`),
  KEY `idx_Key_parent_id` (`parent_res_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2131415868 DEFAULT CHARSET=utf8 COMMENT='权限控制的资源';

DROP TABLE IF EXISTS `t_repo_auth`;
CREATE TABLE `t_repo_auth` (
  `auth_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `creator_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者ID',
  `auth_name` varchar(30) NOT NULL COMMENT '名称',
  `auth_desc` varchar(200) NOT NULL COMMENT '描述',
  `repo_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '仓库类型 1-Git仓库 2-SVN仓库',
  `account` varchar(80) NOT NULL COMMENT '仓库账号',
  `password` varchar(200) DEFAULT '' COMMENT '仓库账号密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`auth_id`),
  KEY `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓库访问配置';


DROP TABLE IF EXISTS `t_role_auth_relation`;
CREATE TABLE `t_repo_auth_relation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `auth_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者ID',
  `role_id` varchar(30) NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_auth_id` (`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓库用户关联';








