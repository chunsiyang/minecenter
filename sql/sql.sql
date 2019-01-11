/*
SQLyog Ultimate
MySQL - 5.7.24 : Database - mine_server
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `sys_permission` */

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id`          varchar(50)  NOT NULL
  COMMENT 'ID',
  `name`        varchar(128) DEFAULT NULL COMMENT '资源名称',
  `per_code`    varchar(128) NOT NULL COMMENT '权限代码字符串',
  `creat_id`    varchar(50)  NOT NULL
  COMMENT '创建人',
  `creat_date`  datetime     NOT NULL
  COMMENT '创建时间',
  `update_id`   varchar(50)  NOT NULL
  COMMENT '更新人',
  `update_date` datetime     NOT NULL
  COMMENT '更新时间',
  `del_flag`    int(1)       NOT NULL DEFAULT '1'
  COMMENT '数据状态(0:删除，1:正常)',
  `remark`      varchar(255) DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `per_code` (`per_code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='资源表';

/*Data for the table `sys_permission` */

insert into `sys_permission` (`id`, `name`, `per_code`, `creat_id`, `creat_date`, `update_id`, `update_date`, `del_flag`, `remark`)
values
  ('1', '查看用户', 'user:view', '1', '2019-01-10 18:34:42', '1', '2019-01-10 18:34:47', 1, NULL),
  ('2', '操作用户', 'user:edit', '1', '2019-01-10 18:34:44', '1', '2019-01-10 18:34:49', 1, NULL);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id`          varchar(50)  NOT NULL
  COMMENT 'ID',
  `name`        varchar(128) NOT NULL COMMENT '角色名称',
  `creat_id`    varchar(50)  NOT NULL
  COMMENT '创建人',
  `creat_date`  datetime     NOT NULL
  COMMENT '创建时间',
  `update_id`   varchar(50)  NOT NULL
  COMMENT '更新人',
  `update_date` datetime     NOT NULL
  COMMENT '更新时间',
  `del_flag`    int(1)       NOT NULL DEFAULT '1'
  COMMENT '数据状态(0:删除，1:正常)',
  `remark`      varchar(255)          DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='角色表';

/*Data for the table `sys_role` */

insert into `sys_role` (`id`, `name`, `creat_id`, `creat_date`, `update_id`, `update_date`, `del_flag`, `remark`) values
  ('1', 'admin', '1', '2019-01-10 18:34:57', '1', '2019-01-10 18:35:07', 1, NULL),
  ('2', 'customer', '1', '2019-01-10 18:34:59', '1', '2019-01-10 18:35:09', 1, NULL);

/*Table structure for table `sys_role_permission` */

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `id`            varchar(50) NOT NULL
  COMMENT 'ID',
  `role_id`       varchar(50)          DEFAULT NULL
  COMMENT '角色id',
  `permission_id` varchar(50)          DEFAULT NULL
  COMMENT '权限id',
  `creat_id`      varchar(50) NOT NULL
  COMMENT '创建人',
  `creat_date`    datetime    NOT NULL
  COMMENT '创建时间',
  `update_id`     varchar(50) NOT NULL
  COMMENT '更新人',
  `update_date`   datetime    NOT NULL
  COMMENT '更新时间',
  `del_flag`      int(1)      NOT NULL DEFAULT '1'
  COMMENT '数据状态(0:删除，1:正常)',
  `remark`        varchar(255)         DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`),
  KEY `permission_id` (`permission_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='角色资源表';

/*Data for the table `sys_role_permission` */

insert into `sys_role_permission` (`id`, `role_id`, `permission_id`, `creat_id`, `creat_date`, `update_id`, `update_date`, `del_flag`, `remark`)
values
  ('1', '1', '1', '1', '2019-01-10 18:35:17', '1', '2019-01-10 18:35:24', 1, NULL),
  ('2', '1', '2', '1', '2019-01-10 18:35:21', '1', '2019-01-10 18:35:26', 1, NULL),
  ('3', '2', '1', '1', '2019-01-10 18:35:19', '1', '2019-01-10 18:35:27', 1, NULL);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id`          varchar(50) NOT NULL
  COMMENT 'ID',
  `account`     varchar(20) DEFAULT NULL COMMENT '帐号',
  `password`    varchar(100) DEFAULT NULL COMMENT '密码',
  `username`    varchar(20) DEFAULT NULL COMMENT '昵称',
  `reg_time`    datetime    NOT NULL COMMENT '注册时间',
  `creat_id`    varchar(50) NOT NULL
  COMMENT '创建人',
  `creat_date`  datetime    NOT NULL
  COMMENT '创建时间',
  `update_id`   varchar(50) NOT NULL
  COMMENT '更新人',
  `update_date` datetime    NOT NULL
  COMMENT '更新时间',
  `del_flag`    int(1) DEFAULT '1'
  COMMENT '数据状态(0:删除，1:正常)',
  `remark`      varchar(255) DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='用户表';

/*Data for the table `sys_user` */

insert into `sys_user` (`id`, `account`, `password`, `username`, `reg_time`, `creat_id`, `creat_date`, `update_id`, `update_date`, `del_flag`, `remark`)
values
  ('1', 'admin', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=', 'admin', '2018-11-27 00:52:04', '1',
    '2019-01-10 18:30:47', '1', '2019-01-10 18:30:49', 1, NULL),
  ('2', 'wang', 'wang', 'wang', '2018-11-27 00:52:04', '1', '2019-01-10 18:31:01', '1', '2019-01-10 18:30:58', 1, NULL),
  ('3', 'guest', 'guest', 'guest', '2018-11-27 00:52:04', '1', '2019-01-10 18:31:05', '1', '2019-01-10 18:31:07', 1,
    NULL);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id`          varchar(50) NOT NULL
  COMMENT 'ID',
  `user_id`     varchar(50)          DEFAULT NULL
  COMMENT '用户id',
  `role_id`     varchar(50)          DEFAULT NULL
  COMMENT '角色id',
  `creat_id`    varchar(50) NOT NULL
  COMMENT '创建人',
  `creat_date`  datetime    NOT NULL
  COMMENT '创建时间',
  `update_id`   varchar(50) NOT NULL
  COMMENT '更新人',
  `update_date` datetime    NOT NULL
  COMMENT '更新时间',
  `del_flag`    int(1)      NOT NULL DEFAULT '1'
  COMMENT '数据状态(0:删除，1:正常)',
  `remark`      varchar(255)         DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='用户角色表';

/*Data for the table `sys_user_role` */

insert into `sys_user_role` (`id`, `user_id`, `role_id`, `creat_id`, `creat_date`, `update_id`, `update_date`, `del_flag`, `remark`)
values
  ('1', '1', '1', '1', '2019-01-10 18:35:35', '1', '2019-01-10 18:35:35', 1, NULL),
  ('2', '2', '2', '1', '2019-01-10 18:35:35', '1', '2019-01-10 18:35:35', 1, NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
